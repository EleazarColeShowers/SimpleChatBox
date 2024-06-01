package com.example.chatboxfirsttrial.data

import com.google.firebase.firestore.PropertyName

data class Message(
    @PropertyName("id") val id: String = "",
    @PropertyName("text") val text: String = "",
    @PropertyName("sender") val sender: String = "",
    @PropertyName("timestamp") val timestamp: Long = 0L
)