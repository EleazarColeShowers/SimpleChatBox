package com.example.chatboxfirsttrial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.chatboxfirsttrial.data.Message
import com.example.chatboxfirsttrial.presentation.ChatViewModel

@Composable
fun MessageItem(message: Message) {
    Row(modifier = Modifier.padding(8.dp)) {
        Text(
            text = message.sender,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(end = 8.dp)
        )
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(Color(0xFF2F9ECE))
                .padding(8.dp)
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}

@Composable
fun MessageList(messages: List<Message>, modifier: Modifier) {
    LazyColumn {
        items(messages.size) { index ->
            MessageItem(message = messages[index])
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputField(onMessageSent: (String) -> Unit) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .padding(8.dp)
            .imePadding()
            .fillMaxWidth()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            placeholder = { Text("Enter message") }
        )
        Button(
            onClick = {
                onMessageSent(text.text)
                text = TextFieldValue("")
                keyboardController?.hide() // Hide the keyboard after sending the message
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Send")
        }
    }
}

@Composable
fun ChatScreen(messages: List<Message>, onMessageSent: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .imePadding()
    ) {
        MessageList(
            messages = messages,
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp) // To avoid overlapping with input field
        )
        Spacer(modifier = Modifier.weight(1f))
        InputField(onMessageSent = onMessageSent)
    }
}
@Composable
fun ChatApp(viewModel: ChatViewModel = ChatViewModel()) {
    val messages by viewModel.messages.collectAsState()

    ChatScreen(messages = messages, onMessageSent = { text ->
        viewModel.sendMessage(text)
    })
}