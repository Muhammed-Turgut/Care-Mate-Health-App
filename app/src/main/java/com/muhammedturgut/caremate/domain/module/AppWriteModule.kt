package com.muhammedturgut.caremate.domain.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.appwrite.Client
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.appwrite.services.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppWriteModule {

    @Provides
    @Singleton
    fun provideAppWriteClient(@ApplicationContext context: Context): Client{
        return Client(context)
            .setEndpoint("https://fra.cloud.appwrite.io/v1")
            .setProject("68c5d4e60032cfd4f984")
    }

    @Provides
    @Singleton
    fun provideDataBase(client: Client) : Databases{
        return Databases(client)
    }

    @Provides
    @Singleton
    fun providesAccount(client: Client): Account{
        return Account(client)
    }

    @Provides
    @Singleton
    fun ProvidesStorage(client: Client) : Storage = Storage(client)

}