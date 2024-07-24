package com.example.chatbot

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel:ViewModel() {

    val messageList by lazy { mutableStateListOf<MessageModel>() }

    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyBC4nYIx0S9xo6K-WCdHQvXwox25dp-g-M")

    fun sendMessage(question:String){
        viewModelScope.launch {

            try {
                val chat =generativeModel.startChat(
                    history = messageList.map {
                        content(it.role){ text(it.message)}
                    }.toList()
                )
                messageList.add(MessageModel(question,role="user"))
                messageList.add(MessageModel("Typing...",role="model"))

                val response = chat.sendMessage(question)
                messageList.removeLast()
                messageList.add(MessageModel(response.text.toString(),role="model"))
            }catch (e:Exception){
                messageList.removeLast()
                messageList.add(MessageModel("Error: ${e.message}",role="model"))
            }



        }
    }
}