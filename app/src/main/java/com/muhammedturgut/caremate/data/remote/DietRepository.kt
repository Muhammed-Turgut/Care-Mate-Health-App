package com.muhammedturgut.caremate.data.remote

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DietRepository @Inject constructor(
    private val apiService: GeminiApiService
) {
    fun fetchDiet(prompt: String, callback: (String?) -> Unit) {
        apiService.getDietList(prompt, callback)
    }
}