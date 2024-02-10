package com.example.testfirebase

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.testfirebase.data.data
import com.example.testfirebase.model.UserModel
import com.example.testfirebase.ui.theme.TestFirebaseTheme
import com.example.testfirebase.view.Nav
import com.example.testfirebase.viewmodel.UserViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    // Declarar authentificación de Firebase
    private lateinit var auth: FirebaseAuth

    private lateinit var userRepository: UserModel
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userRepository = UserModel
        viewModel = UserViewModel(userRepository)
        // Inicializa Firebase
        FirebaseApp.initializeApp(this)
        // Inicializa la instancia de autenticación de Firebase
        auth = Firebase.auth

        setContent {
            TestFirebaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Nav(viewModel = viewModel, auth = auth)
                }
            }
        }
    }

    // onStart()
    override fun onStart() {
        super.onStart()
        // Verifica si el usuario está autenticado
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // El usuario ya está autenticado, obtenemos el id del usuario
            data.userConnectedID.value = currentUser.uid
            // obtenemos el usuario
            viewModel.getUser(currentUser.uid)
            // Cambiamos el estado de conexión del usuario
            if (data.userConnected.value?.connected == false) {
                viewModel.changeConnection(currentUser.uid,true)
            }
        } else {
            // No hay usuario autenticado
        }
    }

    // onStop()
    override fun onStop() {
        super.onStop()
        // Cambiamos el estado de conexión del usuario si está autenticado
        if (data.userConnected.value != null){
            viewModel.changeConnection(data.userConnectedID.value,false)
        }
    }
}