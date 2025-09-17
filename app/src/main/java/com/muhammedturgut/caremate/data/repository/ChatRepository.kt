package com.muhammedturgut.caremate.data.repository

import android.util.Log
import com.muhammedturgut.caremate.data.remote.HFService
import com.muhammedturgut.caremate.data.remote.GradioRequest
import com.muhammedturgut.caremate.data.remote.GradioEventResponse
import okhttp3.ResponseBody
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val hfService: HFService
) {

    fun getPrediction(symptoms: String, callback: (String?, String?) -> Unit) {
        Log.d("ChatRepository", "Starting prediction for: $symptoms")

        val requestBody = GradioRequest(data = listOf(symptoms))

        hfService.startPrediction(requestBody).enqueue(object : Callback<GradioEventResponse> {
            override fun onResponse(call: Call<GradioEventResponse>, response: Response<GradioEventResponse>) {
                Log.d("ChatRepository", "POST Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    val eventId = eventResponse?.event_id

                    Log.d("ChatRepository", "EVENT_ID: $eventId")

                    if (!eventId.isNullOrBlank()) {
                        getPredictionResult(eventId, callback)
                    } else {
                        Log.e("ChatRepository", "No EVENT_ID received")
                        callback(null, "No EVENT_ID received")
                    }
                } else {
                    Log.e("ChatRepository", "POST Error: ${response.code()}")
                    Log.e("ChatRepository", "Error body: ${response.errorBody()?.string()}")
                    callback(null, "HTTP Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GradioEventResponse>, t: Throwable) {
                Log.e("ChatRepository", "POST Network Error: ${t.message}", t)
                callback(null, "Network Error: ${t.message}")
            }
        })
    }

    private fun getPredictionResult(eventId: String, callback: (String?, String?) -> Unit) {
        Log.d("ChatRepository", "Getting result for EVENT_ID: $eventId")

        hfService.getPredictionResult(eventId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("ChatRepository", "GET Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val rawResponse = response.body()?.string()
                    Log.d("ChatRepository", "Raw SSE response length: ${rawResponse?.length}")
                    Log.d("ChatRepository", "Raw SSE response preview: ${rawResponse?.take(200)}")

                    if (!rawResponse.isNullOrBlank()) {
                        val result = parseSSEResponse(rawResponse)
                        if (result != null) {
                            callback(result, null)
                        } else {
                            Log.w("ChatRepository", "SSE parsing failed")
                            callback("API yanıt formatı okunamıyor. Lütfen doktora başvurun.", null)
                        }
                    } else {
                        Log.w("ChatRepository", "Empty response body")
                        callback("API'den boş yanıt geldi. Lütfen tekrar deneyin.", null)
                    }
                } else {
                    Log.e("ChatRepository", "GET HTTP Error: ${response.code()}")
                    Log.e("ChatRepository", "Error body: ${response.errorBody()?.string()}")
                    callback(null, "HTTP Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("ChatRepository", "GET Network Error: ${t.message}", t)
                callback(null, "Network Error: ${t.message}")
            }
        })
    }

    private fun parseSSEResponse(rawResponse: String): String? {
        try {
            Log.d("ChatRepository", "Parsing SSE response...")

            val lines = rawResponse.split("\n")
            val gson = Gson()

            for (line in lines) {
                if (line.startsWith("data: ")) {
                    val jsonData = line.substring(6).trim()
                    Log.d("ChatRepository", "Found JSON data: ${jsonData.take(100)}...")

                    if (jsonData == "[DONE]" || jsonData.isBlank()) {
                        continue
                    }

                    try {
                        // Array olarak parse et
                        val dataArray = gson.fromJson(jsonData, List::class.java) as? List<Any>

                        if (dataArray != null && dataArray.isNotEmpty()) {
                            val result = extractResponseFromArray(dataArray)
                            if (result != null) return cleanResponse(result)
                        }

                        // Object olarak parse et
                        val dataMap = gson.fromJson(jsonData, Map::class.java) as? Map<String, Any>
                        if (dataMap != null) {
                            val result = extractResponseFromMap(dataMap)
                            if (result != null) return cleanResponse(result)
                        }

                        // Son çare: düz string
                        if (jsonData.length > 20) {
                            return cleanResponse(jsonData)
                        }

                    } catch (e: JsonSyntaxException) {
                        Log.w("ChatRepository", "JSON parse error: ${e.message}")
                        if (jsonData.length > 20) {
                            return cleanResponse(jsonData)
                        }
                    }
                }
            }

            Log.w("ChatRepository", "No valid data found in SSE response")
            return null

        } catch (e: Exception) {
            Log.e("ChatRepository", "SSE parsing error: ${e.message}", e)
            return null
        }
    }

    private fun extractResponseFromArray(dataArray: List<Any>): String? {
        try {
            when (val firstElement = dataArray[0]) {
                is Map<*, *> -> {
                    val responseMap = firstElement as? Map<String, Any>
                    val response = responseMap?.get("response") as? String
                    val status = responseMap?.get("status") as? String

                    Log.d("ChatRepository", "Map response - Status: $status, Response length: ${response?.length}")

                    if (status == "success" && !response.isNullOrBlank()) {
                        return response
                    }

                    // Diğer alanları kontrol et
                    val generated = responseMap?.get("generated_text") as? String
                    if (!generated.isNullOrBlank()) return generated

                    val content = responseMap?.get("content") as? String
                    if (!content.isNullOrBlank()) return content
                }
                is String -> {
                    Log.d("ChatRepository", "Direct string response: ${firstElement.take(50)}...")
                    if (firstElement.isNotBlank() && firstElement.length > 10) {
                        return firstElement
                    }
                }
                is List<*> -> {
                    // Chat formatı kontrol et
                    if (dataArray.size >= 2) {
                        val chatHistory = dataArray[1] as? List<List<String>>
                        if (chatHistory != null && chatHistory.isNotEmpty()) {
                            val lastChat = chatHistory.last()
                            if (lastChat.size >= 2 && !lastChat[1].isNullOrBlank()) {
                                return lastChat[1]
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error extracting from array: ${e.message}")
        }
        return null
    }

    private fun extractResponseFromMap(dataMap: Map<String, Any>): String? {
        return listOfNotNull(
            dataMap["response"] as? String,
            dataMap["generated_text"] as? String,
            dataMap["content"] as? String,
            dataMap["text"] as? String
        ).firstOrNull { it.isNotBlank() && it.length > 10 }
    }

    private fun cleanResponse(text: String): String {
        return text
            .replace("\\u015f", "ş")
            .replace("\\u0131", "ı")
            .replace("\\u011f", "ğ")
            .replace("\\u00fc", "ü")
            .replace("\\u00e7", "ç")
            .replace("\\u00f6", "ö")
            .replace("\\u00dc", "Ü")
            .replace("\\u00c7", "Ç")
            .replace("\\u00d6", "Ö")
            .replace("\\u015e", "Ş")
            .replace("\\u0130", "İ")
            .replace("\\u011e", "Ğ")
            .replace("\\n", "\n")
            .replace("\\\"", "\"")
            .trim()
    }
}