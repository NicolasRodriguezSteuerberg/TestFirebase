package com.example.testfirebase.model

import android.util.Log
import com.example.testfirebase.data.data
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.sql.Timestamp

data class Mensaje(
    val destinatario: String = "",
    val emisor: String = "",
    val mensaje: String = "",
    val time: Timestamp? = null
){
    constructor(): this("","","",null)
}


object MessageModel{
    private val db = FirebaseFirestore.getInstance()

    // recupera los mensajes entre dos usuarios
    fun listenForMessages(user1: String,user2: String) = callbackFlow<List<Mensaje>> {
         val listener = db.collection("messages")
            .whereEqualTo("destinatario", listOf(user1,user2))
            .whereEqualTo("emisor", listOf(user1,user2))
            .orderBy("fecha")
            .limit(50)
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

    // añade un mensaje
    fun addMessage(message: Mensaje) {
        db.collection("messages").add(message)
    }
}