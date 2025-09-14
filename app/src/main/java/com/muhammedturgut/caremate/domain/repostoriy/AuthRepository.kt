package com.muhammedturgut.caremate.domain.repostoriy

interface AuthRepository {
    suspend fun login(email:String, password:String): Result<Unit>
    suspend fun register(email: String,password: String, userId:String): Result<Unit>
    suspend fun isUserLoggedIn() : Boolean
}