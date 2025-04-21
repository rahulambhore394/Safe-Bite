package com.rahul.safebite.chatBot


data class GeminiRequest(
    var contents: List<Content> = listOf()
) {
    data class Content(
        var role: String = "",
        var parts: List<Part> = listOf()
    ) {
        data class Part(
            var text: String = ""
        )
    }
}
