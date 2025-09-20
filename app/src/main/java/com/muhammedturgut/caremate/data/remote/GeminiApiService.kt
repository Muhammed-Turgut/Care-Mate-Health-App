package com.muhammedturgut.caremate.data.remote

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiApiService @Inject constructor(private val client: OkHttpClient) {

    private val apiKey = "AIzaSyAMP2E27IVd7qeFe09uX4eBCrNN8Ci1lz0"
    private val TAG = "GeminiApiService"

    fun getDietList(prompt: String, callback: (String?) -> Unit) {
        // API key kontrolü
        if (apiKey.isEmpty()) {
            Log.e(TAG, "API key boş!")
            callback("API anahtarı eksik")
            return
        }

        // Prompt kontrolü ve temizleme
        if (prompt.trim().isEmpty()) {
            callback("Lütfen kullanıcı bilgilerini girin")
            return
        }

        val cleanPrompt = prompt.replace("\"", "\\\"").replace("\n", "\\n")

        // Doğru Gemini API endpoint (v1beta kullanın)
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"

        Log.d(TAG, "API URL: $url")
        Log.d(TAG, "Prompt: $cleanPrompt")

        val requestBody = """
            {
                "contents": [
                    {
                        "parts": [
                            {
                                "text": "$cleanPrompt"
                            }
                        ]
                    }
                ],
                "generationConfig": {
                    "temperature": 0.7,
                    "topK": 40,
                    "topP": 0.95,
                    "maxOutputTokens": 2048
                },
                "safetySettings": [
                    {
                        "category": "HARM_CATEGORY_HARASSMENT",
                        "threshold": "BLOCK_MEDIUM_AND_ABOVE"
                    },
                    {
                        "category": "HARM_CATEGORY_HATE_SPEECH",
                        "threshold": "BLOCK_MEDIUM_AND_ABOVE"
                    },
                    {
                        "category": "HARM_CATEGORY_SEXUALLY_EXPLICIT",
                        "threshold": "BLOCK_MEDIUM_AND_ABOVE"
                    },
                    {
                        "category": "HARM_CATEGORY_DANGEROUS_CONTENT",
                        "threshold": "BLOCK_MEDIUM_AND_ABOVE"
                    }
                ]
            }
        """.trimIndent()

        Log.d(TAG, "Request Body: $requestBody")

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .post(RequestBody.create("application/json".toMediaTypeOrNull(), requestBody))
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Network hatası: ${e.message}")
                callback("Ağ bağlantısı hatası: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d(TAG, "Response Code: ${response.code}")
                Log.d(TAG, "Response Message: ${response.message}")
                Log.d(TAG, "Response Body: $responseBody")

                when (response.code) {
                    200 -> {
                        try {
                            val parsedResponse = parseGeminiResponse(responseBody)
                            callback(parsedResponse)
                        } catch (e: Exception) {
                            Log.e(TAG, "JSON parse hatası: ${e.message}")
                            callback("Yanıt işlenirken hata oluştu: ${e.message}")
                        }
                    }
                    400 -> {
                        Log.e(TAG, "400 - Bad Request: $responseBody")
                        callback("Geçersiz istek. API formatını kontrol edin.")
                    }
                    403 -> {
                        Log.e(TAG, "403 - Forbidden: $responseBody")
                        val errorMessage = parseErrorMessage(responseBody)
                        callback("Yetkilendirme hatası: $errorMessage")
                    }
                    429 -> {
                        Log.e(TAG, "429 - Too Many Requests")
                        callback("Çok fazla istek. Lütfen biraz bekleyin.")
                    }
                    else -> {
                        Log.e(TAG, "HTTP Error ${response.code}: $responseBody")
                        callback("API hatası: ${response.code} - ${response.message}")
                    }
                }
            }
        })
    }

    private fun parseGeminiResponse(responseBody: String?): String? {
        if (responseBody == null) return null

        return try {
            val jsonObject = JSONObject(responseBody)
            Log.d(TAG, "Parsing response JSON")

            if (jsonObject.has("candidates")) {
                val candidates = jsonObject.getJSONArray("candidates")
                if (candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)

                    if (firstCandidate.has("content")) {
                        val content = firstCandidate.getJSONObject("content")
                        val parts = content.getJSONArray("parts")
                        if (parts.length() > 0) {
                            val text = parts.getJSONObject(0).getString("text")
                            Log.d(TAG, "Başarılı yanıt alındı")
                            return text
                        }
                    }
                }
            }

            if (jsonObject.has("error")) {
                val error = jsonObject.getJSONObject("error")
                val errorMessage = error.getString("message")
                Log.e(TAG, "API Error: $errorMessage")
                return "API Hatası: $errorMessage"
            }

            "Geçersiz yanıt formatı"
        } catch (e: Exception) {
            Log.e(TAG, "Parse error: ${e.message}")
            "Yanıt parse edilemedi: ${e.message}"
        }
    }

    private fun parseErrorMessage(responseBody: String?): String {
        return try {
            if (responseBody != null) {
                val jsonObject = JSONObject(responseBody)
                if (jsonObject.has("error")) {
                    val error = jsonObject.getJSONObject("error")
                    return error.getString("message")
                }
            }
            "API anahtarı geçersiz veya süresi dolmuş"
        } catch (e: Exception) {
            "API anahtarı problemi"
        }
    }
}