package com.example.testfirebase.model

import android.util.Log
import com.example.testfirebase.data.data
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

data class User(
    val email: String = "",
    val nombre: String = "",
    var edad: Int = 0,
    var connected: Boolean = false
){
    // Constructor vacío requerido por Firebase
    constructor() : this("", "",0, false)
}

object UserModel{
    private val db = FirebaseFirestore.getInstance()

    // Escucha cambios en la colección "usuarios"
    fun listenForUserChanges() = callbackFlow<List<User>> {
        // inicializa el listener
        val listener = db.collection("table_prueba")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
                val users = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(User::class.java)
                } ?: emptyList()
                trySend(users).isSuccess
            }
        awaitClose { listener.remove() }
    }

    fun addUser(user: User, userId: String): Task<Void> {
        // añadir usuario con id del authuser
        val userDocument = db.collection("table_prueba").document(userId)
        Log.d("addUser", userDocument.toString())
        Log.d("addUser", user.toString())
        return userDocument.set(user)
    }

    fun getIdUser(user:User){
        // Obtenemos el id del usuario que queremos actualizar
        db.collection("table_prueba")
            .whereEqualTo("nombre", user.nombre)
            .whereEqualTo("edad", user.edad)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    val document = task.result?.documents?.get(0)
                    data.documentId.value = document?.id.toString()
                }
            }
    }

    fun updateUser(documentId: String, user: User): Task<Void> {
        // Actualiza un usuario existente en la colección "usuarios"
        return db.collection("table_prueba").document(documentId).set(user, SetOptions.merge())
    }

    suspend fun getUserConnected(id:String){
        db.collection("table_prueba").document(id).get().addOnSuccessListener { document ->
            if (document != null) {
                val user = document.toObject(User::class.java)
                if (user != null) {
                    data.userConnected.value = user
                    Log.d("miUser", user.toString())
                } else {
                    data.userConnected.value = null
                }
            } else {
                data.userConnected.value = null
            }
        }
    }

    fun changeConnection(id: String?, connected: Boolean){
        Log.d("miUser", id.toString() + " " + connected.toString())
        if (id != null) {
            db.collection("table_prueba").document(id).update("connected", connected)
            data.userConnected.value?.connected = connected
            Log.d("miUser", data.userConnected.value.toString())
        }
    }
}