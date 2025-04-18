package com.rahul.safebite.chatBot

data class ChatMessage(
    val message: String,
    val type: Int
) {
    companion object {
        const val TYPE_SENT = 0   // User message
        const val TYPE_RECEIVED = 1 // Bot message
    }
}
