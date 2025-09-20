package com.muhammedturgut.caremate.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diet_table")
data class DietItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "day")
    val day: String,

    //Sabah kahvaltısı
    @ColumnInfo(name = "breakfast_calorie")
    val breakfastCalorie: String,

    @ColumnInfo(name = "breakfast_one_food")
    val breakfastOneFood: String,

    @ColumnInfo(name = "breakfast_two_food")
    val breakfastTwoFood: String,

    //Öğle yemeği

    @ColumnInfo(name = "lunch_calorie")
    val lunchCalorie: String,

    @ColumnInfo(name = "lunch_one_food")
    val lunchOneFood: String,

    @ColumnInfo(name = "lunch_two_food")
    val lunchTwoFood: String,

    //Akşam Ymeği
    @ColumnInfo(name = "evening_meal_calorie")
    val eveningMealCalorie: String,

    @ColumnInfo(name = "evening_neal_one_food")
    val eveningMealOneFood: String,

    @ColumnInfo(name = "evening_meal_two_food")
    val eveningMealTwoFood: String,


)
