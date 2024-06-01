package com.example.chatboxfirsttrial.presentation

import androidx.lifecycle.ViewModel
import com.example.chatboxfirsttrial.data.Message
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages

    init {
        listenForMessages()
    }

    fun sendMessage(text: String) {
        val newMessage = Message(
            id = UUID.randomUUID().toString(),
            text = text,
            sender = "Me",
            timestamp = System.currentTimeMillis()
        )
        sendMessageToFirestore(newMessage)
    }

    private fun sendMessageToFirestore(message: Message) {
        val db = FirebaseFirestore.getInstance()
        db.collection("messages").add(message)
    }

    private fun listenForMessages() {
        val db = FirebaseFirestore.getInstance()
        db.collection("messages").orderBy("timestamp").addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) {
                return@addSnapshotListener
            }

            val messages = snapshot.documents.mapNotNull { it.toObject(Message::class.java) }
            _messages.value = messages
        }
    }
}