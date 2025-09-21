package com.muhammedturgut.caremate.ui.aiChat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammedturgut.caremate.data.remote.model.ChatMessage
import com.muhammedturgut.caremate.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    companion object {
        private const val TAG = "ChatViewModel"
        private const val MIN_MESSAGE_LENGTH = 3
    }

    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _getAiMessage = MutableLiveData<ChatMessage?>(null)
    val getAiMessage: LiveData<ChatMessage?> = _getAiMessage

    // SADECE API'dan gelen cevapları tek tek almak için
    private val _getApiResponse = MutableLiveData<String?>(null)
    val getApiResponse: LiveData<String?> = _getApiResponse

    // Konuşma geçmişi
    private val conversationHistory = mutableListOf<ChatMessage>()

    init {
        Log.d(TAG, "ChatViewModel başlatıldı")
        addWelcomeMessage()
    }

    private fun addWelcomeMessage() {
        val welcomeMessage = ChatMessage(
            text = "Merhaba! Ben AI Doktor asistanınızım. Belirtilerinizi detaylı olarak anlatın, size yardımcı olmaya çalışayım. Lütfen unutmayın, bu bilgiler tıbbi tavsiye yerine geçmez.",
            isUser = false
        )
        conversationHistory.add(welcomeMessage)
        _messages.value = conversationHistory.toList()

        // Hoş geldin mesajını _getAiMessage'a ekle ama _getApiResponse'a EKLEME
        _getAiMessage.value = welcomeMessage

        Log.d(TAG, "Hoş geldin mesajı eklendi")
    }

    fun sendMessage(userText: String) {
        val trimmedText = userText.trim()

        // Mesaj validasyonu
        if (!validateMessage(trimmedText)) return

        Log.d(TAG, "Mesaj gönderiliyor: '${trimmedText.take(50)}...'")

        viewModelScope.launch {
            try {
                // Loading başlat
                setLoadingState(true)

                // Kullanıcı mesajını ekle
                addUserMessage(trimmedText)

                // API çağrısı yap
                val result = chatRepository.sendMessageWithHistory(conversationHistory)

                // Sonucu işle
                handleApiResponse(result)

            } catch (e: Exception) {
                Log.e(TAG, "Beklenmeyen hata: ${e.message}", e)
                handleError("Beklenmeyen bir hata oluştu. Lütfen tekrar deneyin.")
            } finally {
                setLoadingState(false)
            }
        }
    }

    private fun validateMessage(text: String): Boolean {
        return when {
            text.isBlank() -> {
                Log.w(TAG, "Boş mesaj gönderme denemesi")
                false
            }
            text.length < MIN_MESSAGE_LENGTH -> {
                Log.w(TAG, "Çok kısa mesaj: ${text.length} karakter")
                _error.value = "Lütfen daha detaylı açıklayın"
                false
            }
            else -> true
        }
    }

    private fun addUserMessage(text: String) {
        val userMessage = ChatMessage(text = text, isUser = true)
        conversationHistory.add(userMessage)
        updateUI()
        Log.d(TAG, "Kullanıcı mesajı eklendi. Toplam: ${conversationHistory.size}")
    }

    private fun handleApiResponse(result: Result<String>) {
        result.fold(
            onSuccess = { response ->
                Log.d(TAG, "API yanıtı başarılı: ${response.length} karakter")
                val aiMessage = ChatMessage(text = response, isUser = false)
                conversationHistory.add(aiMessage)
                updateUI()
                clearError()

                // Tüm AI mesajlarını emit et
                _getAiMessage.value = aiMessage

                // SADECE API cevabını tek tek emit et (String olarak)
                _getApiResponse.value = response
                Log.d(TAG, "🎯 API cevabı tek tek emit edildi: '${response.take(50)}...'")
            },
            onFailure = { exception ->
                Log.e(TAG, "API hatası: ${exception.message}")
                handleError(exception.message ?: "Yanıt alınamadı")
                val errorMessage = addErrorMessage("Üzgünüm, şu anda yanıt veremiyorum. Lütfen tekrar deneyin.")

                // Hata mesajını _getAiMessage'a ekle
                _getAiMessage.value = errorMessage

                // API cevabı başarısız olduğu için null emit et
                _getApiResponse.value = null
                Log.d(TAG, "❌ API hatası - null emit edildi")
            }
        )
    }

    private fun handleError(errorMessage: String) {
        _error.value = errorMessage
        Log.e(TAG, "Hata durumu ayarlandı: $errorMessage")
    }

    private fun addErrorMessage(text: String): ChatMessage {
        val errorMessage = ChatMessage(text = text, isUser = false)
        conversationHistory.add(errorMessage)
        updateUI()
        return errorMessage
    }

    private fun setLoadingState(isLoading: Boolean) {
        _isLoading.value = isLoading
        Log.d(TAG, "Loading durumu: $isLoading")
    }

    private fun clearError() {
        _error.value = null
    }

    private fun updateUI() {
        _messages.value = conversationHistory.toList()
    }

    fun clearChat() {
        Log.d(TAG, "Chat temizleniyor...")
        conversationHistory.clear()
        clearError()

        // State'leri temizle
        _getAiMessage.value = null
        _getApiResponse.value = null

        addWelcomeMessage() // Hoş geldin mesajını tekrar ekle
        Log.d(TAG, "Chat temizlendi ve hoş geldin mesajı eklendi")
    }

    // API cevabını manuel olarak temizlemek için
    fun clearApiResponse() {
        _getApiResponse.value = null
        Log.d(TAG, "API response state'i temizlendi")
    }

    // AI mesajını manuel olarak temizlemek için
    fun clearAiMessage() {
        _getAiMessage.value = null
        Log.d(TAG, "AI mesaj state'i temizlendi")
    }

    // Son API cevabını almak için
    fun getLastApiResponse(): String? {
        return _getApiResponse.value
    }

    fun retryLastMessage() {
        if (conversationHistory.isNotEmpty()) {
            val lastUserMessage = conversationHistory.findLast { it.isUser }
            lastUserMessage?.let { message ->
                Log.d(TAG, "Son mesaj tekrar gönderiliyor: '${message.text.take(50)}...'")
                sendMessage(message.text)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ChatViewModel temizlendi")
    }
}