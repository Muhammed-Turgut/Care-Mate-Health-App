package com.muhammedturgut.caremate.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muhammedturgut.caremate.data.local.entity.ChatData
import com.muhammedturgut.caremate.data.local.entity.DailyUserData
import com.muhammedturgut.caremate.data.local.entity.DietItem
import com.muhammedturgut.caremate.data.local.entity.PostureAnalysis


@Database(
    entities = [
          DailyUserData::class,
          ChatData::class,
          PostureAnalysis::class,
          DietItem::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseDao: RoomDatabase(){
    abstract fun itemDao() : ItemDao
}