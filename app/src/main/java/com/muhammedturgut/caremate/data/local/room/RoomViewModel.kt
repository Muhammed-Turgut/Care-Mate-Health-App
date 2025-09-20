package com.muhammedturgut.caremate.data.local.room


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammedturgut.caremate.data.local.entity.ChatData
import com.muhammedturgut.caremate.data.local.entity.DailyUserData
import com.muhammedturgut.caremate.data.local.entity.DietItem
import com.muhammedturgut.caremate.data.local.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.catch

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val TAG = "RoomViewModel"

    // Chat Data
    private var _getAllChatItemList = MutableStateFlow<List<ChatData>>(emptyList())
    val getAllItemChatList: StateFlow<List<ChatData>> = _getAllChatItemList

    // Diet Items - Nullable'ları kaldırdım
    private val _dietItems = MutableStateFlow<List<DietItem>>(emptyList())
    val dietItems: StateFlow<List<DietItem>> = _dietItems

    // Daily User Data
    private val _dailyUserData = MutableStateFlow<DailyUserData?>(null)
    val dailyUserData: StateFlow<DailyUserData?> = _dailyUserData

    // Loading ve Error State'leri ekledim
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    private val _currentDayDiet = MutableStateFlow<DietItem?>(null)
    val currentDayDiet: StateFlow<DietItem?> = _currentDayDiet.asStateFlow()




    init {
        loadDailyUserData()
        getDietList() // Bu çağrıyı ekledim
        getDietItemByDay()
    }

    fun loadDailyUserData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                roomRepository.getDailyUserDataItem().collect { item ->
                    _dailyUserData.value = item
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
                Log.e(TAG, "loadDailyUserData error: ${e.message}")
            }
        }
    }

    fun insertChatData(send: String, contentMessage: String, date: String) {
        viewModelScope.launch {
            try {
                roomRepository.insertChatData(send, contentMessage, date)
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "insertChatData error: ${e.message}")
            }
        }
    }

    fun deleteAllChatDataItem() {
        viewModelScope.launch {
            try {
                roomRepository.deleteAllChatDataItem()
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "deleteAllChatDataItem error: ${e.message}")
            }
        }
    }

    fun insertDailyUserData(
        amountOfWaterConsumedDaily: String,
        numberOfStepsTakenDaily: String,
        howDoYouFeelToday: String,
        todaySleepDuration: String,
        id: Int
    ) {
        viewModelScope.launch {
            try {
                roomRepository.insertDailyUserData(
                    amountOfWaterConsumedDaily,
                    numberOfStepsTakenDaily,
                    howDoYouFeelToday,
                    todaySleepDuration,
                    id
                )
                // Insert sonrası reload
                loadDailyUserData()
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "insertDailyUserData error: ${e.message}")
            }
        }
    }

    fun updateDailyUserData(
        amountOfWaterConsumedDaily: String,
        numberOfStepsTakenDaily: String,
        howDoYouFeelToday: String,
        todaySleepDuration: String,
        id: Int
    ) {
        viewModelScope.launch {
            try {
                roomRepository.updateDailyUserData(
                    amountOfWaterConsumedDaily,
                    numberOfStepsTakenDaily,
                    howDoYouFeelToday,
                    todaySleepDuration,
                    id
                )
                // Update sonrası reload
                loadDailyUserData()
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "updateDailyUserData error: ${e.message}")
            }
        }
    }

    fun getDietList() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                roomRepository.getDietList().collect { list ->
                    // Null'ları filtrele
                    _dietItems.value = list.filterNotNull()
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
                Log.e(TAG, "getDietList error: ${e.message}")
            }
        }
    }

    // DietItem object ile çağırma - daha clean
    fun dietListInsert(dietItem: DietItem) {
        viewModelScope.launch {
            try {
                roomRepository.dietListInsert(
                    dietItem.day,
                    dietItem.breakfastCalorie,
                    dietItem.breakfastOneFood,
                    dietItem.breakfastTwoFood,
                    dietItem.lunchCalorie,
                    dietItem.lunchOneFood,
                    dietItem.lunchTwoFood,
                    dietItem.eveningMealCalorie,
                    dietItem.eveningMealOneFood,
                    dietItem.eveningMealTwoFood
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "dietListInsert error: ${e.message}")
            }
        }
    }

    // Eski method'u da koruyalım - backward compatibility
    fun dietListInsert(
        day: String,
        breakfastCalorie: String,
        breakfastOneFood: String,
        breakfastTwoFood: String,
        lunchCalorie: String,
        lunchOneFood: String,
        lunchTwoFood: String,
        eveningMealCalorie: String,
        eveningMealOneFood: String,
        eveningMealTwoFood: String
    ) {
        viewModelScope.launch {
            try {
                roomRepository.dietListInsert(
                    day, breakfastCalorie, breakfastOneFood, breakfastTwoFood,
                    lunchCalorie, lunchOneFood, lunchTwoFood,
                    eveningMealCalorie, eveningMealOneFood, eveningMealTwoFood
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "dietListInsert error: ${e.message}")
            }
        }
    }

    fun dietListUpdate(
        day: String,
        breakfastCalorie: String,
        breakfastOneFood: String,
        breakfastTwoFood: String,
        lunchCalorie: String,
        lunchOneFood: String,
        lunchTwoFood: String,
        eveningMealCalorie: String,
        eveningMealOneFood: String,
        eveningMealTwoFood: String
    ) {
        viewModelScope.launch {
            try {
                roomRepository.dietListUpdate(
                    day, breakfastCalorie, breakfastOneFood, breakfastTwoFood,
                    lunchCalorie, lunchOneFood, lunchTwoFood,
                    eveningMealCalorie, eveningMealOneFood, eveningMealTwoFood
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e(TAG, "dietListUpdate error: ${e.message}")
            }
        }
    }

    fun deleteAllDietItems(){
        viewModelScope.launch {
            try {

                roomRepository.deleteAllDietItems()

            }catch (e: Exception){

                Log.d(TAG,e.message.toString())

            }
        }
    }

    fun getDietItemByDay() {
        viewModelScope.launch {
            try {
                // Repository'de suspend fun olmalı
                val dietItem = roomRepository.getDietItemByDay(day = getTodayInTurkish())
                _currentDayDiet.value = dietItem
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                _currentDayDiet.value = null
            }
        }
    }

}

fun getTodayInTurkish(): String {
    val today = LocalDate.now()
    val dayOfWeek = today.dayOfWeek.value

    return when (dayOfWeek) {
        1 -> "Pazartesi"
        2 -> "Salı"
        3 -> "Çarşamba"
        4 -> "Perşembe"
        5 -> "Cuma"
        6 -> "Cumartesi"
        7 -> "Pazar"
        else -> "Bilinmiyor"
    }
}