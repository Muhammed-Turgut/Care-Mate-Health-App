package com.muhammedturgut.caremate.data.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

// /predict endpoint'i için (tek parametre)
data class GradioRequest(
    val data: List<String>
)

// /health_chat_api endpoint'i için (iki parametre)
data class GradioChatRequest(
    val data: List<Any> // [String, List<List<Any>>]
)

// POST response - EVENT_ID döner
data class GradioEventResponse(
    val event_id: String? = null,
    val error: String? = null
)

interface HFService {

    // /predict endpoint'i - Tek parametre (belirtiler)
    @POST("gradio_api/call/predict")
    @Headers("Content-Type: application/json")
    fun startPrediction(
        @Body request: GradioRequest
    ): Call<GradioEventResponse>

    // /health_chat_api endpoint'i - İki parametre (mesaj + chat geçmişi)
    @POST("gradio_api/call/health_chat_api")
    @Headers("Content-Type: application/json")
    fun startChatPrediction(
        @Body request: GradioChatRequest
    ): Call<GradioEventResponse>

    // GET - Sonuç alma (her iki endpoint için aynı)
    @GET("gradio_api/call/predict/{eventId}")
    @Headers("Accept: text/event-stream")
    fun getPredictionResult(
        @Path("eventId") eventId: String
    ): Call<okhttp3.ResponseBody>

    // GET - Chat sonuç alma
    @GET("gradio_api/call/health_chat_api/{eventId}")
    @Headers("Accept: text/event-stream")
    fun getChatPredictionResult(
        @Path("eventId") eventId: String
    ): Call<okhttp3.ResponseBody>
}