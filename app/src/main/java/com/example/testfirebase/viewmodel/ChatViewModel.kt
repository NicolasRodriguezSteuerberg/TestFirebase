package com.example.testfirebase.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testfirebase.data.data
import com.example.testfirebase.model.MessageModel
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class ChatViewModel(private val userRepository: MessageModel): ViewModel() {


    // Escucha cambios en la colección de mensajes
     fun listenForMessages(destinatario: String, emisor: String) {
        data.messagesList.value = emptyList()
        viewModelScope.launch {
            // Escucha cambios en la colección de mensajes
            userRepository.listenForMessages(destinatario, emisor).collect { messageList ->
                // Actualiza la lista de mensajes
                data.messagesList.value = messageList
            }
        }
    }


}