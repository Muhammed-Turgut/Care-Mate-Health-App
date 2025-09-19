package com.muhammedturgut.caremate.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posture_analysis")
data class PostureAnalysis(

    //Burası bel düzleşmesi ile ilgili verilerin tutulduğu alan.
    @ColumnInfo(name = "curvature_general_condition")
    val curvatureGeneralCondition : String,

    @ColumnInfo(name = "curvature_spine")
    val curvatureSpine : String,

    @ColumnInfo(name = "measured_angle_curvature")
    val measuredAngleCurvature : String,

    @ColumnInfo(name = "normal_range_curvature")
    val normalRangeCurvature : String,

    @ColumnInfo(name = "curvature_text")
    val curvatureText : String,

    //Burası boyun düzleşmesi ile ilgili verilerin tutulduğu alan.

    @ColumnInfo(name = "neck_general_condition")
    val neckGeneralCondition : String,

    @ColumnInfo(name = "neck_spine")
    val neckSpine : String,

    @ColumnInfo(name = "measured_angle_neck")
    val measuredAngleNeck : String,

    @ColumnInfo(name = "normal_range_neck ")
    val normalRangeNeck : String,

    @ColumnInfo(name = "neck_text")
    val curvatureTextNeck : String,

    ){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
