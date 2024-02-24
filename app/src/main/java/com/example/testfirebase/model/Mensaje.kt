package com.example.testfirebase.model

import android.util.Log
import com.example.testfirebase.data.data
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.util.Date

data class Mensaje(
    val destinatario: String = "",
    val emisor: String = "",
    val mensaje: String = "",
    val time: Date? = null
){
    constructor(): this("","","",null)
}


object MessageModel{
    private val db = FirebaseFirestore.getInstance()

    // recupera los mensajes entre dos usuarios
    fun listenForMessages(user1: String,user2: String) = callbackFlow<List<Mensaje>> {
         val listener = db.collection("messages")
            .whereIn("destinatario", listOf(user1, user2))
            .whereIn("emisor", listOf(user1, user2))
            .orderBy("time")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(Mensaje::class.java)
                } ?: emptyList()

                trySend(messages).isSuccess
            }
        awaitClose { listener.remove() }
    }

    /*
    fun loadMessages(user1: String,user2: String){
        db.collection("messages")
            .whereEqualTo("destinatario", user1)
            .whereEqualTo("emisor", user2)
            .get()
            .addOnSuccessListener { result ->
                for(document in result){
                    val message = document.toObject(Mensaje::class.java)
                    data.messagesList.add(message)
                }
            }
        Log.d("ChatScreen", "messagesList: ${data.messagesList.toList()}")
    }*/

    // añade un mensaje
    fun addMessage(message: Mensaje) {
        db.collection("messages").add(message)
    }
}