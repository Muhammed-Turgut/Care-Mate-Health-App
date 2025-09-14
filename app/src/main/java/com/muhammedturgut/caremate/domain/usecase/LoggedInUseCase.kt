package com.muhammedturgut.caremate.domain.usecase

import com.muhammedturgut.caremate.domain.repostoriy.AuthRepository
import javax.inject.Inject

class LoggedInUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Boolean{
        return authRepository.isUserLoggedIn()
    }
}