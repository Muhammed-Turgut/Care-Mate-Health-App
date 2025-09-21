package com.muhammedturgut.caremate.data.remote.model



data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)