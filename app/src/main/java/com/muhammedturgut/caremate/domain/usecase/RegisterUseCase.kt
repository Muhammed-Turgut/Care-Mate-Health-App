package com.muhammedturgut.caremate.domain.usecase

import com.muhammedturgut.caremate.domain.repostoriy.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email:String , password:String, userId:String) : Result<Unit>{
        return authRepository.register(email = email, password = password,userId=userId)
    }
}