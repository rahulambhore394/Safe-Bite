package com.rahul.safebite.chatBot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.rahul.safebite.R

class ChatAdapter(private val context: Context, private val messageList: List<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messageList[position]

        // Reset both views
        holder.cardViewBot.visibility = View.GONE
        holder.cardViewUser.visibility = View.GONE

        if (message.type == ChatMessage.TYPE_RECEIVED) {
            holder.cardViewBot.visibility = View.VISIBLE
            holder.messageTextViewBot.text = message.message
        } else if (message.type == ChatMessage.TYPE_SENT) {
            holder.cardViewUser.visibility = View.VISIBLE
            holder.messageTextViewUser.text = message.message
        }
    }




    override fun getItemCount(): Int {
        return messageList.size
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextViewBot: TextView = itemView.findViewById(R.id.message_text_by_bot)
        val messageTextViewUser: TextView = itemView.findViewById(R.id.message_text_by_user)
        val cardViewBot: CardView = itemView.findViewById(R.id.card1)
        val cardViewUser: CardView = itemView.findViewById(R.id.card2)
    }
}
