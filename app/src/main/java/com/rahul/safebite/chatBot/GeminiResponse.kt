package com.rahul.safebite.chatBot


data class GeminiResponse(
    val candidates: List<Candidate>? = null
) {
    data class Candidate(
        val content: Content? = null
    ) {
        data class Content(
            val parts: List<Part>? = null
        ) {
            data class Part(
                val text: String? = null
            )
        }
    }
}

