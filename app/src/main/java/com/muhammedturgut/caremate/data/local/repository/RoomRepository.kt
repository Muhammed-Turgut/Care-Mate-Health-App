package com.muhammedturgut.caremate.data.local.repository

import com.muhammedturgut.caremate.data.local.dataSource.RoomDataSource
import com.muhammedturgut.caremate.data.local.entity.ChatData
import com.muhammedturgut.caremate.data.local.entity.DailyUserData
import com.muhammedturgut.caremate.data.local.entity.DietItem
import com.muhammedturgut.caremate.data.local.entity.PostureAnalysis
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepository @Inject constructor(val roomDataSource: RoomDataSource) {

    fun getAllChatDataItem() : Flow<List<ChatData>>{
        return roomDataSource.getAllChatDataItem()
    }

    suspend fun insertChatData( send: String, contentsMessage: String , date: String){
        roomDataSource.insertChatData(send,contentsMessage,date)
    }

    suspend fun deleteAllChatDataItem(){
        roomDataSource.deleteAllChatDataItem()
    }

     fun getDailyUserDataItem() : Flow <DailyUserData?> {
        return  roomDataSource.getDailyUserDataItem()
    }

    suspend fun insertDailyUserData(amountOfWaterConsumedDaily: String,
                                    numberOfStepsTakenDaily: String,
                                    howDoYouFeelToday: String,
                                    todaySleepDuration: String,
                                    id:Int
    ){



        roomDataSource.insertDailyUserData(amountOfWaterConsumedDaily,
            numberOfStepsTakenDaily,
            howDoYouFeelToday,
            todaySleepDuration,
            id
            )
    }

    suspend fun updateDailyUserData(amountOfWaterConsumedDaily: String,
                                    numberOfStepsTakenDaily: String,
                                    howDoYouFeelToday: String,
                                    todaySleepDuration: String,
                                    id:Int){



        roomDataSource.updateDailyUserData(amountOfWaterConsumedDaily,
            numberOfStepsTakenDaily,
            howDoYouFeelToday,
            todaySleepDuration,
            id)
    }

    suspend fun getPostureAnalysis(id:Int) : PostureAnalysis?{

        return roomDataSource.getPostureAnalysis(id)

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


        roomDataSource.insertPostureAnalysisItem(curvatureGeneralCondition,
            curvatureSpine,
            measuredAngleCurvature,
            normalRangeCurvature,
            curvatureText,
            neckGeneralCondition,
            neckSpine,
            measuredAngleNeck,
            normalRangeNeck,
            curvatureTextNeck)
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

        roomDataSource.updatePostureAnalysisItem(curvatureGeneralCondition,
            curvatureSpine,
            measuredAngleCurvature,
            normalRangeCurvature,
            curvatureText,
            neckGeneralCondition,
            neckSpine,
            measuredAngleNeck,
            normalRangeNeck,
            curvatureTextNeck)
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

        roomDataSource.deletePostureAnalysisItem(curvatureGeneralCondition,
            curvatureSpine,
            measuredAngleCurvature,
            normalRangeCurvature,
            curvatureText,
            neckGeneralCondition,
            neckSpine,
            measuredAngleNeck,
            normalRangeNeck,
            curvatureTextNeck)
    }


    fun getDietList(): Flow<List<DietItem?>> {
        return roomDataSource.getDietList()
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
        roomDataSource.dietListInsert(
            day, breakfastCalorie, breakfastOneFood, breakfastTwoFood,
            lunchCalorie, lunchOneFood, lunchTwoFood,
            eveningMealCalorie, eveningMealOneFood, eveningMealTwoFood
        )
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
        roomDataSource.dietListUpdate(
            day, breakfastCalorie, breakfastOneFood, breakfastTwoFood,
            lunchCalorie, lunchOneFood, lunchTwoFood,
            eveningMealCalorie, eveningMealOneFood, eveningMealTwoFood
        )
    }

    suspend fun deleteAllDietItems(){
        roomDataSource.deleteAllDietItems()
    }

    suspend fun getDietItemByDay(day: String): DietItem? {
        return roomDataSource.getDietItemByDay(day)
    }

}