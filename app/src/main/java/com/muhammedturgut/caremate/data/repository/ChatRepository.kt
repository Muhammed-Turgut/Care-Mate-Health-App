package com.muhammedturgut.caremate.data.repository

import android.util.Log
import com.muhammedturgut.caremate.data.remote.Content
import com.muhammedturgut.caremate.data.remote.GeminiApi
import com.muhammedturgut.caremate.data.remote.GeminiRequest
import com.muhammedturgut.caremate.data.remote.Part
import com.muhammedturgut.caremate.data.remote.model.ChatMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val api: GeminiApi,
    private val apiKey: String
) {

    companion object {
        private const val TAG = "ChatRepository"
        private const val MAX_HISTORY_SIZE = 10
        private const val MAX_MESSAGE_LENGTH = 1000
    }

    suspend fun sendMessageWithHistory(conversationHistory: List<ChatMessage>): Result<String> {
        return try {
            Log.d(TAG, "=== API İSTEĞİ BAŞLIYOR ===")
            Log.d(TAG, "Mesaj sayısı: ${conversationHistory.size}")
            Log.d(TAG, "API Key uzunluğu: ${apiKey.length}")

            // Sadece son mesajları al
            val limitedHistory = conversationHistory.takeLast(MAX_HISTORY_SIZE)
            Log.d(TAG, "Sınırlanmış mesaj sayısı: ${limitedHistory.size}")

            // Contents listesi oluştur
            val contents = buildContents(limitedHistory)
            Log.d(TAG, "Contents oluşturuldu: ${contents.size} adet")

            // İlk content'in text uzunluğunu logla
            contents.firstOrNull()?.parts?.firstOrNull()?.text?.let { text ->
                Log.d(TAG, "İlk content uzunluğu: ${text.length}")
                Log.d(TAG, "İlk content önizleme: ${text.take(200)}...")
            }

            // API isteği oluştur
            val request = GeminiRequest(contents = contents)
            Log.d(TAG, "Request oluşturuldu")

            // API çağrısı yap
            Log.d(TAG, "API çağrısı yapılıyor...")
            val response = api.generate(apiKey, request)
            Log.d(TAG, "API yanıtı alındı. Candidates: ${response.candidates?.size}")

            // Yanıtı parse et
            val result = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text

            if (result.isNullOrBlank()) {
                Log.e(TAG, "API yanıtı boş geldi")
                Result.failure(Exception("Yanıt alınamadı"))
            } else {
                Log.d(TAG, "✅ Başarılı yanıt: ${result.length} karakter")
                Result.success(result.trim())
            }

        } catch (e: Exception) {
            Log.e(TAG, "❌ API hatası: ${e.message}", e)

            // HTTP response body varsa logla
            if (e.message?.contains("HTTP") == true) {
                Log.e(TAG, "HTTP hatası detayı: ${e.message}")
            }

            Result.failure(handleApiError(e))
        }
    }

    private fun buildContents(conversationHistory: List<ChatMessage>): List<Content> {
        val contents = mutableListOf<Content>()

        // Son kullanıcı mesajını al
        val lastUserMessage = conversationHistory.lastOrNull { it.isUser }

        if (lastUserMessage != null) {
            // Sistem promptu ile kullanıcı mesajını birleştir
            val combinedPrompt = buildCombinedPrompt(lastUserMessage.text, conversationHistory)

            contents.add(
                Content(
                    parts = listOf(Part(text = combinedPrompt))
                )
            )

            Log.d(TAG, "Combined prompt oluşturuldu: ${combinedPrompt.length} karakter")
        }

        return contents
    }

    private fun buildCombinedPrompt(userMessage: String, history: List<ChatMessage>): String {
        val systemPrompt = getSystemPrompt()

        // Önceki mesajlardan context oluştur (son 3 mesaj çifti)
        val recentHistory = history.takeLast(6).filter { !it.isUser || it.text != userMessage }
        val contextBuilder = StringBuilder()

        if (recentHistory.isNotEmpty()) {
            contextBuilder.append("\n\nÖnceki konuşma:\n")
            recentHistory.forEach { message ->
                val role = if (message.isUser) "Kullanıcı" else "AI Doktor"
                contextBuilder.append("$role: ${message.text}\n")
            }
        }

        return """
            $systemPrompt
            $contextBuilder
            
            Şimdi bu kullanıcı mesajına yanıt ver:
            Kullanıcı: $userMessage
        """.trimIndent()
    }

    private fun getSystemPrompt(): String {
        return """
            Sen profesyonel bir AI doktor asistanısın. Kısa, net ve yardımcı yanıtlar ver.
            
            Kurallar:
            - Türkçe yanıt ver
            - Kesinlikle teşhis koyma
            - Ciddi belirtiler için doktora başvurulmasını öner
            - Empati göster ve anlayışlı ol
            - Her yanıtın sonunda "Bu bilgiler tıbbi tavsiye yerine geçmez" ekle
            
            Kullanıcının sağlık sorunlarına yardımcı ol.
        """.trimIndent()
    }

    private fun handleApiError(e: Exception): Exception {
        return when {
            e.message?.contains("400") == true ->
                Exception("Geçersiz istek. Lütfen tekrar deneyin.")
            e.message?.contains("401") == true ->
                Exception("API anahtarı hatası. Lütfen daha sonra tekrar deneyin.")
            e.message?.contains("403") == true ->
                Exception("Erişim reddedildi. Lütfen daha sonra tekrar deneyin.")
            e.message?.contains("429") == true ->
                Exception("Çok fazla istek gönderildi. Lütfen biraz bekleyin.")
            e.message?.contains("timeout") == true ->
                Exception("Bağlantı zaman aşımına uğradı. İnternet bağlantınızı kontrol edin.")
            e.message?.contains("Unable to resolve host") == true ->
                Exception("İnternet bağlantınızı kontrol edin.")
            else ->
                Exception("Bir hata oluştu: ${e.message ?: "Bilinmeyen hata"}")
        }
    }
}