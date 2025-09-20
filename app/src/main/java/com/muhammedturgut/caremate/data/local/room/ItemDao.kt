package com.muhammedturgut.caremate.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.muhammedturgut.caremate.data.local.entity.ChatData
import com.muhammedturgut.caremate.data.local.entity.DailyUserData
import com.muhammedturgut.caremate.data.local.entity.DietItem
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
    fun getDailyUserDataItem(id: Int) : Flow <DailyUserData?>

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


    //Diet listesinin fonksiyonları
    @Query("SELECT * FROM diet_table ORDER BY day ASC")
    fun getAllDietItems(): Flow<List<DietItem?>>

    // Deprecated - artık kullanmayın
    @Query("SELECT * FROM diet_table WHERE id = :id")
    fun getDietList(id: Int): Flow<List<DietItem?>>

    // Gün ismine göre diet item getir
    @Query("SELECT * FROM diet_table WHERE day = :day LIMIT 1")
    suspend fun getDietItemByDay(day: String): DietItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun dietListInsert(item: DietItem)

    @Update
    suspend fun dietListUpdate(item: DietItem)

    // Yeni eklenen fonksiyonlar
    @Delete
    suspend fun deleteDietItem(item: DietItem)

    @Query("DELETE FROM diet_table")
    suspend fun deleteAllDietItems()

    // ID'ye göre diet item getir
    @Query("SELECT * FROM diet_table WHERE id = :id")
    suspend fun getDietItemById(id: Int): DietItem?

    // Specific günleri getir
    @Query("SELECT * FROM diet_table WHERE day IN (:days)")
    fun getDietItemsByDays(days: List<String>): Flow<List<DietItem>>

    // Diet item var mı kontrolü
    @Query("SELECT COUNT(*) FROM diet_table WHERE day = :day")
    suspend fun isDietItemExistsForDay(day: String): Int


}