package com.rahul.safebite.chatBot

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rahul.safebite.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatBotActivity : AppCompatActivity() {

    private lateinit var userInput: EditText
    private lateinit var sendButton: Button
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private val messageList = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        userInput = findViewById(R.id.userInputEditText)
        sendButton = findViewById(R.id.sendButton)
        chatRecyclerView = findViewById(R.id.chatRecyclerView)

        chatAdapter = ChatAdapter(this, messageList)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        sendButton.setOnClickListener {
            val userMessage = userInput.text.toString()
            if (!TextUtils.isEmpty(userMessage)) {
                addMessageToChat(userMessage, ChatMessage.TYPE_SENT)
                sendMessageToApi(userMessage)
                userInput.text.clear()
            }
        }
    }

    private fun sendMessageToApi(userQuery: String) {
        // Append instruction to ensure the response is species-related
        val modifiedQuery = """
            Please provide information only about species. 
            This includes details about its scientific name, common name, habitat, whether it's venomous or not, 
            its bite symptoms, and where it is commonly found.
            Ignore all other topics.
            
            User query: $userQuery
        """.trimIndent()

        val request = GeminiRequest().apply {
            contents = listOf(
                GeminiRequest.Content().apply {
                    role = "user"
                    parts = listOf(GeminiRequest.Content.Part().apply { text = modifiedQuery })
                }
            )
        }

        val apiService = ApiClient.getClient().create(GeminiApiService::class.java)
        val call = apiService.getChatResponse("AIzaSyCCyAoLvEUwK7P-ExhPMvTj5US1w6iBM_k", request)

        call.enqueue(object : Callback<GeminiResponse> {
            override fun onResponse(call: Call<GeminiResponse>, response: Response<GeminiResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val botResponse = response.body()!!.candidates?.get(0)?.content?.parts?.get(0)?.text

                    // Validate if response is related to species
                    if (isSpeciesRelated(botResponse)) {
                        addMessageToChat(botResponse ?: "Sorry, I couldn't understand that.", ChatMessage.TYPE_RECEIVED)
                    } else {
                        addMessageToChat("I can only provide information related to species and their bites, habitats, and venom status.", ChatMessage.TYPE_RECEIVED)
                    }
                } else {
                    Toast.makeText(this@ChatBotActivity, "Error: Unable to get response", Toast.LENGTH_SHORT).show()
                    Log.e("ChatBot", "Response error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<GeminiResponse>, t: Throwable) {
                Toast.makeText(this@ChatBotActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("ChatBot", "API call failed: ${t.message}")
            }
        })
    }

    private fun isSpeciesRelated(responseText: String?): Boolean {
        if (responseText == null) return false

        val keywords = listOf("species", "scientific name", "habitat", "venomous", "bite", "symptoms", "found in")

        // Check if response contains at least one keyword
        return keywords.any { responseText.contains(it, ignoreCase = true) }
    }

    private fun addMessageToChat(message: String, type: Int) {
        messageList.add(ChatMessage(message, type))
        chatAdapter.notifyItemInserted(messageList.size - 1)
        chatRecyclerView.scrollToPosition(messageList.size - 1)
    }

}
