package com.muhammedturgut.caremate.data.remote


import com.muhammedturgut.caremate.domain.repostoriy.AuthRepository
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppWriteAuthRepositoryImpl @Inject constructor(client: Client) : AuthRepository {
    private val account = Account(client)

    override suspend fun login(
        email:String,
        password :String
    ): Result<Unit>{
        return try {
            withContext(Dispatchers.IO){
                account.createEmailPasswordSession(email,password)
                Result.success(Unit)
            }
        }
        catch (e: AppwriteException){
            Result.failure(e)
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        userId: String
    ): Result<Unit> {
        return try {
            withContext(Dispatchers.IO){

                account.create(userId = userId,
                    email=email,
                    password=password)

                kotlinx.coroutines.delay(1000)
                account.createEmailPasswordSession(email,password)
                Result.success(Unit)


            }
        }
        catch (e: AppwriteException){
            Result.failure(e)
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return try {
            val user = withContext(Dispatchers.IO){
                account.get()
            }
            user.id.isNotEmpty()
        }
        catch (e: Exception){
            false
        }

    }
}