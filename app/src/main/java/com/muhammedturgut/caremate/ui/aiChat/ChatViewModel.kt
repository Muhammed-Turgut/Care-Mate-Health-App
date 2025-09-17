package com.muhammedturgut.caremate.ui.aiChat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammedturgut.caremate.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _connectionStatus = MutableLiveData("Hazır")
    val connectionStatus: LiveData<String> = _connectionStatus

    init {
        // Başlangıç mesajı ekle
        addMessage(ChatMessage(
            "🏥 CareMate Sağlık Asistanınıza hoş geldiniz!\n\nBelirtilerinizi detaylı bir şekilde yazın, size yardımcı olmaya çalışayım.\n\n⚠️ Bu tıbbi tanı değildir. Kesin tanı için doktora başvurun.",
            isUser = false
        ))
    }

    fun sendMessage(symptoms: String) {
        if (symptoms.isBlank() || _isLoading.value == true) {
            Log.w("ChatViewModel", "Empty symptoms or already loading")
            return
        }

        Log.d("ChatViewModel", "Sending symptoms: $symptoms")

        // Kullanıcı mesajını ekle
        addMessage(ChatMessage(symptoms.trim(), isUser = true))

        // Loading durumunu ayarla
        _isLoading.value = true
        _error.value = null
        _connectionStatus.value = "API'ye bağlanılıyor..."

        // API çağrısı yap
        repository.getPrediction(symptoms.trim()) { result, errorMsg ->

            // UI thread'de güncelleme yap
            _isLoading.postValue(false)
            _connectionStatus.postValue("Tamamlandı")

            if (errorMsg != null) {
                Log.e("ChatViewModel", "API Error: $errorMsg")
                _error.postValue(errorMsg)
                _connectionStatus.postValue("Hata: $errorMsg")

                // Hata durumunda varsayılan yardım mesajı
                val errorMessage = """
Üzgünüm, şu anda API'ye bağlanamıyorum. 

**Genel Öneriler:**
• Belirtileriniz ciddi ise doktora başvurun
• Bol su için ve dinlenin
• Ateş varsa vücut ısınızı kontrol edin
• Acil durumlarda 112'yi arayın

**⚠️ Önemli:** Bu tıbbi tanı değildir.

Hata detayı: $errorMsg
                """.trimIndent()

                addMessage(ChatMessage(errorMessage, isUser = false))

            } else if (result != null) {
                Log.d("ChatViewModel", "API Success - Response length: ${result.length}")
                _connectionStatus.postValue("Başarılı")

                // Başarılı yanıtı ekle
                addMessage(ChatMessage(result, isUser = false))

                // Takip sorusu ekle
                addMessage(ChatMessage(
                    "\n💡 Başka belirtileriniz var mı? Varsa lütfen belirtin.",
                    isUser = false
                ))

            } else {
                Log.w("ChatViewModel", "No result and no error - unexpected state")
                _error.postValue("Beklenmeyen durum")

                addMessage(ChatMessage(
                    "Beklenmeyen bir durum oluştu. Lütfen tekrar deneyin veya doktora başvurun.",
                    isUser = false
                ))
            }
        }
    }

    private fun addMessage(message: ChatMessage) {
        val currentMessages = _messages.value.orEmpty().toMutableList()
        currentMessages.add(message)
        _messages.value = currentMessages

        Log.d("ChatViewModel", "Message added - Total: ${currentMessages.size}, IsUser: ${message.isUser}, Text preview: ${message.text.take(50)}...")
    }

    fun clearChat() {
        Log.d("ChatViewModel", "Clearing chat")

        _messages.value = listOf(
            ChatMessage(
                "🤖 Chat temizlendi. AI ile yeni konuşma başlayabilirsiniz.\n\n⚠️ Bu tıbbi tanı değildir. Kesin tanı için doktora başvurun.",
                isUser = false
            )
        )
        _error.value = null
        _connectionStatus.value = "Hazır"
    }

    fun retryLastMessage() {
        val messages = _messages.value.orEmpty()
        val lastUserMessage = messages.findLast { it.isUser }

        if (lastUserMessage != null) {
            Log.d("ChatViewModel", "Retrying last message: ${lastUserMessage.text}")
            sendMessage(lastUserMessage.text)
        }
    }

    // Test için
    fun testConnection() {
        _connectionStatus.value = "Test ediliyor..."
        sendMessage("test belirtisi")
    }
}

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val id: String = "${System.currentTimeMillis()}_${(0..1000).random()}"
)