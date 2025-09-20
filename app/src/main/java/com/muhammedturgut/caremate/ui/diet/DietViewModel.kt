package com.muhammedturgut.caremate.ui.diet

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class DietViewModel @Inject constructor() : ViewModel() {

    private val _getCurrentMealTime = MutableStateFlow("")
    val getCurrentMealTimeInformation: StateFlow<String> = _getCurrentMealTime.asStateFlow()

    init {
        // StateFlow'a değer ata
        _getCurrentMealTime.value = getCurrentMealTime().displayName
    }

    // İstenen saat aralıkları ile öğün zamanı tespiti
    private fun getCurrentMealTime(): MealTime {
        val currentTime = LocalTime.now()
        val hour = currentTime.hour

        return when (hour) {
            in 6..10 -> MealTime.SABAH      // 06:00 - 10:59
            in 11..15 -> MealTime.OGLE      // 11:00 - 15:59
            else -> MealTime.AKSAM          // 16:00 - 05:59 (geri kalan tüm zaman)
        }
    }

    // Public fonksiyon - güncellenmiş değer almak için
    fun updateCurrentMealTime() {
        _getCurrentMealTime.value = getCurrentMealTime().displayName
    }
}

enum class MealTime(val displayName: String) {
    SABAH("Breakfast"),
    OGLE("Lunch"),
    AKSAM("Evening meal")
}