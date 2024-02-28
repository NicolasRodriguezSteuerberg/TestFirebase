package com.example.testfirebase.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.testfirebase.data.data
import com.example.testfirebase.model.User
import com.example.testfirebase.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserModel): ViewModel() {

    init {
        // Escuchar cambios en la colección de usuarios
        listenForUserChanges()
    }

    private fun listenForUserChanges() {
        viewModelScope.launch {
            // Escucha cambios en la colección de usuarios
            userRepository.listenForUserChanges().collect { userList ->
                // Actualiza la lista de usuarios
                data.users.value = userList
            }
        }
    }

    fun addUser(user: User, navController: NavController) {
        if (data.userConnectedID.value != null) {
            Log.d("miUser", data.userConnectedID.value + " " + data.userConnected.value.toString())

            var addUserTask = userRepository.addUser(user.copy(connected = true), data.userConnectedID.value)
            Log.d("add", addUserTask.toString())
            data.nombre.value = ""
            data.edad.value = ""
            data.userConnected.value = user
            Log.d("miUser", user.toString())
            changeConnection(data.userConnectedID.value,true)
            navController.navigate("logged")
        } else{
            data.error.value = "No user connected"
            navController.navigate("registerAuth")
        }
    }

    // recupera un usuario
    fun getUser(id: String){
        userRepository.getUserConnected(id)
        Log.d("startAuth", data.userConnected.value.toString())
    }

    fun changeConnection(id: String, connected: Boolean){
        Log.d("miUser", data.userConnected.value.toString() + " " + data.userConnectedID.value)
        Log.d("miUser", "cambio a: " + connected)
        userRepository.changeConnection(id, connected)
        Log.d("miUser", data.userConnected.value.toString() + " " + data.userConnectedID.value)

    }

    fun registerAuthUser(email: String, password: String, auth: FirebaseAuth, navController: NavController){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    data.userConnectedID.value = user?.uid.toString()
                    data.email.value = ""
                    data.password.value = ""
                    navController.navigate("registerUser")
                } else {
                    // If sign in fails, display a message to the user.
                    data.error.value = "Authentication failed."
                }
            }
        Log.d("TAG", "createUserWithEmail:success")
        Log.d("TAG", data.userConnectedID.value)
    }

    // Inicia sesión con correo y contraseña
    fun login(email: String, password: String, auth: FirebaseAuth, navController: NavController){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    data.userConnectedID.value = user?.uid.toString()
                    data.email.value = ""
                    data.password.value = ""
                    getUser(data.userConnectedID.value)
                    changeConnection(data.userConnectedID.value,true)
                    navController.navigate("logged")
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        navController.context,
                        "LOGIN INCORRECTO",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun deleteUser(email: String){
        userRepository.deleteUser(email)
    }

    fun changeUser(navController: NavController) {
        userRepository.updateUser(data.userConnected.value?.email.toString(),data.nombre.value,data.edad.value.toInt())
        navController.navigate("logged")
    }
}