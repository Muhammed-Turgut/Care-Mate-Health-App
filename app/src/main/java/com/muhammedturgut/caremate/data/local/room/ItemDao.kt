package com.muhammedturgut.caremate.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.muhammedturgut.caremate.data.local.entity.ChatData
import com.muhammedturgut.caremate.data.local.entity.DailyUserData
import com.muhammedturgut.caremate.data.local.entity.PostureAnalysis
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM chat_data")
    fun getAllChatDataItem() : Flow<List<ChatData>>

    @Insert
    suspend fun insertChatData(item: ChatData)

    @Query("DELETE FROM chat_data")
    suspend fun deleteAllChatDataItem () //Chat ekranındaki tüm mesajları siler.

    //Burda kullanıcının günlük aktivitelerinin verileri var.

    @Query("SELECT * FROM daily_user_data WHERE id = :id")
    suspend fun getDailyUserDataItem(id: Int) : DailyUserData?

    @Insert
    suspend fun insertDailyUserData(item: DailyUserData)

    @Update
    suspend fun updateDailyUserData(item: DailyUserData)

    //Burda kullanıcının postür analizne ait verilerin tutulduğu yer.

    @Query("SELECT * FROM posture_analysis WHERE id = :id")
    suspend fun getPostureAnalysis(id: Int) : PostureAnalysis?

    @Insert
    suspend fun insertPostureAnalysisItem(item: PostureAnalysis)

    @Update
    suspend fun updatePostureAnalysisItem(item: PostureAnalysis)

    @Delete
    suspend fun deletePostureAnalysisItem(item : PostureAnalysis)


}