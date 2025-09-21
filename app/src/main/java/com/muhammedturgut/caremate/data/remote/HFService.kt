package com.muhammedturgut.caremate.data.remote


import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

data class GeminiRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)

data class GeminiResponse(
    val candidates: List<Candidate>?
)

data class Candidate(
    val content: Content?
)

interface GeminiApi {
    @POST("models/gemini-1.5-flash:generateContent")
    suspend fun generate(
        @Query("key") key: String,
        @Body body: GeminiRequest
    ): GeminiResponse
}