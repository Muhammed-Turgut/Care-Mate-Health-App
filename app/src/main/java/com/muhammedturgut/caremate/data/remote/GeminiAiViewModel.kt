package com.muhammedturgut.caremate.data.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GeminiAiViewModel @Inject constructor(private val repo: DietRepository) : ViewModel() {

    private val _dietText = MutableStateFlow("Henüz diyet oluşturulmadı")
    val dietText: StateFlow<String> = _dietText.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun generateDiet(userInfo: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // Daha detaylı prompt oluşturma
            val detailedPrompt = createDietPrompt(userInfo)

            withContext(Dispatchers.IO) {
                repo.fetchDiet(detailedPrompt) { result ->
                    // Ana thread'de UI güncellemesi
                    viewModelScope.launch(Dispatchers.Main) {
                        _isLoading.value = false
                        if (result != null && result.isNotEmpty()) {
                            _dietText.value = result
                            _error.value = null
                        } else {
                            _error.value = "Diyet oluşturulurken bir hata oluştu. Lütfen tekrar deneyin."
                            _dietText.value = "Henüz diyet oluşturulmadı"
                        }
                    }
                }
            }
        }
    }

    private fun createDietPrompt(userInfo: String): String {
        return """
            Lütfen aşağıdaki bilgilere göre detaylı bir haftalık diyet listesi oluştur:
            
            Kullanıcı Bilgileri: $userInfo
            
            Diyet listesi aşağıdaki kriterlere uygun olsun:
            - Günlük 3 ana öğün ve 2 ara öğün
            - Kalori değerleri belirtilsin
            - Türk mutfağına uygun yemekler
            - Sağlıklı ve dengeli beslenme
            - Pratik ve uygulanabilir tarifler
            
            Lütfen yanıtı Türkçe olarak ver ve düzenli bir format kullan.
        """.trimIndent()
    }

    fun clearError() {
        _error.value = null
    }
}