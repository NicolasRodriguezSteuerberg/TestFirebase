package com.example.testfirebase.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testfirebase.data.data
import com.example.testfirebase.model.Mensaje
import com.example.testfirebase.model.MessageModel
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch
import java.util.Date

class ChatViewModel(private val userRepository: MessageModel): ViewModel() {


    // Escucha cambios en la colección de mensajes
     fun listenForMessages(destinatario: String, emisor: String) {
        viewModelScope.launch {
            // Escucha cambios en la colección de mensajes
            userRepository.listenForMessages(destinatario, emisor).collect { messageList ->
                data.messagesList.value = messageList
            }
        }
    }

    /*
    fun loadMessages(destinatario: String, emisor: String){
        viewModelScope.launch {
            userRepository.loadMessages(destinatario, emisor)
        }
    }*/

    fun addMessage(destinatario: String, emisor: String, mensaje: String){
        userRepository.addMessage(message = Mensaje(destinatario, emisor, mensaje, Date()))
    }

}