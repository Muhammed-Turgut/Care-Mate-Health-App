package com.muhammedturgut.caremate.data.local.repository

import com.muhammedturgut.caremate.data.local.dataSource.RoomDataSource
import com.muhammedturgut.caremate.data.local.entity.ChatData
import com.muhammedturgut.caremate.data.local.entity.DailyUserData
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

    suspend fun getDailyUserDataItem() : DailyUserData? {
        return  roomDataSource.getDailyUserDataItem()
    }

    suspend fun insertDailyUserData(amountOfWaterConsumedDaily: String,
                                    numberOfStepsTakenDaily: String,
                                    howDoYouFeelToday: String,
                                    todaySleepDuration: String
    ){



        roomDataSource.insertDailyUserData(amountOfWaterConsumedDaily,
            numberOfStepsTakenDaily,
            howDoYouFeelToday,
            todaySleepDuration
            )
    }

    suspend fun updateDailyUserData(amountOfWaterConsumedDaily: String,
                                    numberOfStepsTakenDaily: String,
                                    howDoYouFeelToday: String,
                                    todaySleepDuration: String){



        roomDataSource.updateDailyUserData(amountOfWaterConsumedDaily,
            numberOfStepsTakenDaily,
            howDoYouFeelToday,
            todaySleepDuration)
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

}