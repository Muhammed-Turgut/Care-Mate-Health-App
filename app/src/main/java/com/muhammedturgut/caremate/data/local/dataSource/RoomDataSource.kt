package com.muhammedturgut.caremate.data.local.dataSource

import com.muhammedturgut.caremate.data.local.entity.ChatData
import com.muhammedturgut.caremate.data.local.entity.DailyUserData
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

    suspend fun getDailyUserDataItem() : DailyUserData? {
        return  itemDao.getDailyUserDataItem(id = 1)
    }

    suspend fun insertDailyUserData(amountOfWaterConsumedDaily: String,
                                    numberOfStepsTakenDaily: String,
                                    howDoYouFeelToday: String,
                                    todaySleepDuration: String
                                    ){

        val newItem = DailyUserData(
            amountOfWaterConsumedDaily,
            numberOfStepsTakenDaily,
            howDoYouFeelToday,
            todaySleepDuration)

        itemDao.insertDailyUserData(newItem)
    }

    suspend fun updateDailyUserData(amountOfWaterConsumedDaily: String,
                                    numberOfStepsTakenDaily: String,
                                    howDoYouFeelToday: String,
                                    todaySleepDuration: String){

        val newItem = DailyUserData(
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
}