package com.muhammedturgut.caremate.data.local.room


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammedturgut.caremate.data.local.entity.ChatData
import com.muhammedturgut.caremate.data.local.repository.RoomRepository
import com.muhammedturgut.caremate.ui.aiChat.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(val roomRepository: RoomRepository) : ViewModel(){
    private val _getAllChatItemList = MutableStateFlow<List<ChatData>>(emptyList())
    val getAllItemChatList: StateFlow<List<ChatData>> = _getAllChatItemList



    fun insertChatData(send: String, contentMessage : String, date: String){

        viewModelScope.launch {
            try {
                roomRepository.insertChatData(send,contentMessage,date)
            }
            catch (e: Exception){
                Log.d("RoomViewModel",e.message.toString())
            }

        }

    }

    fun deleteAllChatDataItem(){
        viewModelScope.launch {
            try {
                roomRepository.deleteAllChatDataItem()
            }
            catch (e: Exception){
                Log.d("RoomViewModel",e.message.toString())
            }
        }
    }

    fun insertDailyUserData(amountOfWaterConsumedDaily: String,
                            numberOfStepsTakenDaily: String,
                            howDoYouFeelToday: String,
                            todaySleepDuration: String){
        viewModelScope.launch {
            try {

                roomRepository.insertDailyUserData(amountOfWaterConsumedDaily,
                    numberOfStepsTakenDaily,
                    howDoYouFeelToday,
                    todaySleepDuration
                    )

            }catch (e: Exception){
                Log.d("RoomViewModel", e.message.toString())
            }
        }
    }
}