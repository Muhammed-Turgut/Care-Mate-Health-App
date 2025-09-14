package com.muhammedturgut.caremate.domain.module


import com.muhammedturgut.caremate.data.remote.AppWriteAuthRepositoryImpl
import com.muhammedturgut.caremate.domain.repostoriy.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        appWriteAuthRepositoryImpl: AppWriteAuthRepositoryImpl
    ): AuthRepository
}