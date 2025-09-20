package com.muhammedturgut.caremate.data.local.dataSource

import com.muhammedturgut.caremate.data.local.entity.ChatData
import com.muhammedturgut.caremate.data.local.entity.DailyUserData
import com.muhammedturgut.caremate.data.local.entity.DietItem
import com.muhammedturgut.caremate.data.local.entity.PostureAnalysis
import com.muhammedturgut.caremate.data.local.room.ItemDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDataSource @Inject constructor(val itemDao: ItemDao) {

     fun getAllChatDataItem() : Flow<List<ChatData>>{
         return itemDao.getAllChatDataItem()
    }

    suspend fun insertChatData( send: String, contentsMessage: String , date: String){
        val newItem = ChatData(send,contentsMessage,date)

        itemDao.insertChatData(newItem)
    }

    suspend fun deleteAllChatDataItem(){
        itemDao.deleteAllChatDataItem()
    }

     fun getDailyUserDataItem() : Flow <DailyUserData?> {
        return  itemDao.getDailyUserDataItem(id = 1)
    }

    suspend fun insertDailyUserData(amountOfWaterConsumedDaily: String,
                                    numberOfStepsTakenDaily: String,
                                    howDoYouFeelToday: String,
                                    todaySleepDuration: String,
                                    id:Int
                                    ){

        val newItem = DailyUserData(
            id,
            amountOfWaterConsumedDaily,
            numberOfStepsTakenDaily,
            howDoYouFeelToday,
            todaySleepDuration)

        itemDao.insertDailyUserData(newItem)
    }

    suspend fun updateDailyUserData(amountOfWaterConsumedDaily: String,
                                    numberOfStepsTakenDaily: String,
                                    howDoYouFeelToday: String,
                                    todaySleepDuration: String,
                                    id:Int){

        val newItem = DailyUserData(
            id,
            amountOfWaterConsumedDaily,
            numberOfStepsTakenDaily,
            howDoYouFeelToday,
            todaySleepDuration)

        itemDao.updateDailyUserData(newItem)
    }

    suspend fun getPostureAnalysis(id:Int) : PostureAnalysis?{

        return itemDao.getPostureAnalysis(id)

    }

    suspend fun insertPostureAnalysisItem(curvatureGeneralCondition: String,
                                          curvatureSpine: String,
                                          measuredAngleCurvature: String,
                                          normalRangeCurvature: String,
                                          curvatureText: String,
                                          neckGeneralCondition: String,
                                          neckSpine: String,
                                          measuredAngleNeck: String,
                                          normalRangeNeck: String,
                                          curvatureTextNeck: String
                                          ){
        val newItem = PostureAnalysis( curvatureGeneralCondition,
            curvatureSpine,
            measuredAngleCurvature,
            normalRangeCurvature,
            curvatureText,
            neckGeneralCondition,
            neckSpine,
            measuredAngleNeck,
            normalRangeNeck,
            curvatureTextNeck
        )

        itemDao.insertPostureAnalysisItem(newItem)
    }

    suspend fun updatePostureAnalysisItem(curvatureGeneralCondition: String,
                                          curvatureSpine: String,
                                          measuredAngleCurvature: String,
                                          normalRangeCurvature: String,
                                          curvatureText: String,
                                          neckGeneralCondition: String,
                                          neckSpine: String,
                                          measuredAngleNeck: String,
                                          normalRangeNeck: String,
                                          curvatureTextNeck: String
    ) {
        val newItem = PostureAnalysis(
            curvatureGeneralCondition,
            curvatureSpine,
            measuredAngleCurvature,
            normalRangeCurvature,
            curvatureText,
            neckGeneralCondition,
            neckSpine,
            measuredAngleNeck,
            normalRangeNeck,
            curvatureTextNeck
        )
        itemDao.updatePostureAnalysisItem(newItem)
    }

    suspend fun deletePostureAnalysisItem(curvatureGeneralCondition: String,
                                          curvatureSpine: String,
                                          measuredAngleCurvature: String,
                                          normalRangeCurvature: String,
                                          curvatureText: String,
                                          neckGeneralCondition: String,
                                          neckSpine: String,
                                          measuredAngleNeck: String,
                                          normalRangeNeck: String,
                                          curvatureTextNeck: String
    ) {
        val newItem = PostureAnalysis(
            curvatureGeneralCondition,
            curvatureSpine,
            measuredAngleCurvature,
            normalRangeCurvature,
            curvatureText,
            neckGeneralCondition,
            neckSpine,
            measuredAngleNeck,
            normalRangeNeck,
            curvatureTextNeck
        )
        itemDao.deletePostureAnalysisItem(newItem)
    }


    fun getDietList(): Flow<List<DietItem?>> {
        // ID'yi sabit 1 yerine tüm diet items'ları getir
        return itemDao.getAllDietItems()
    }

    suspend fun dietListInsert(
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
        val newItem = DietItem(
            id = 0, // AutoGenerate için 0 kullan
            day = day,
            breakfastCalorie = breakfastCalorie,
            breakfastOneFood = breakfastOneFood,
            breakfastTwoFood = breakfastTwoFood,
            lunchCalorie = lunchCalorie,
            lunchOneFood = lunchOneFood,
            lunchTwoFood = lunchTwoFood,
            eveningMealCalorie = eveningMealCalorie,
            eveningMealOneFood = eveningMealOneFood,
            eveningMealTwoFood = eveningMealTwoFood
        )
        itemDao.dietListInsert(newItem)
    }

    suspend fun dietListUpdate(
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
        // Önce mevcut item'ı bul
        val existingItem = itemDao.getDietItemByDay(day)

        if (existingItem != null) {
            val updatedItem = existingItem.copy(
                breakfastCalorie = breakfastCalorie,
                breakfastOneFood = breakfastOneFood,
                breakfastTwoFood = breakfastTwoFood,
                lunchCalorie = lunchCalorie,
                lunchOneFood = lunchOneFood,
                lunchTwoFood = lunchTwoFood,
                eveningMealCalorie = eveningMealCalorie,
                eveningMealOneFood = eveningMealOneFood,
                eveningMealTwoFood = eveningMealTwoFood
            )
            itemDao.dietListUpdate(updatedItem)
        } else {
            // Eğer mevcut değilse, yeni oluştur
            dietListInsert(
                day, breakfastCalorie, breakfastOneFood, breakfastTwoFood,
                lunchCalorie, lunchOneFood, lunchTwoFood,
                eveningMealCalorie, eveningMealOneFood, eveningMealTwoFood
            )
        }
    }

    suspend fun deleteAllDietItems(){
        itemDao.deleteAllDietItems()
    }

    suspend fun getDietItemByDay(day: String): DietItem?{

           return itemDao.getDietItemByDay(day)
    }

}