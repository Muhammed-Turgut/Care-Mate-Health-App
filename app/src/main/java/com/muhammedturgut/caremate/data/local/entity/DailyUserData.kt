package com.muhammedturgut.caremate.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_user_data")
data class DailyUserData(

    @ColumnInfo(name = "amount_of_water_consumed_daily")
    val amountOfWaterConsumedDaily: String,

    @ColumnInfo(name = "number_of_steps_taken_daily")
    val numberOfStepsTakenDaily: String,

    @ColumnInfo(name = "how_do_you_feel_today?")
    val howDoYouFeelToday: String,

    @ColumnInfo(name = "today_sleep_duration")
    val todaySleepDuration: String,

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}