package com.muhammedturgut.caremate.data.local.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): DatabaseDao {
        return Room.databaseBuilder(
            context.applicationContext,
            DatabaseDao::class.java,
            "care_mate_database"
        ).build()
    }

    @Provides
    fun provideItemDao(database: DatabaseDao): ItemDao {
        return database.itemDao()
    }
}