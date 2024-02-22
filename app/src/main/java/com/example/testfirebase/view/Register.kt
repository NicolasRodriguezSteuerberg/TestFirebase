package com.example.testfirebase.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.testfirebase.data.data
import com.example.testfirebase.model.User
import com.example.testfirebase.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterAuthScreen(navController: NavController, viewModel: UserViewModel, auth: FirebaseAuth) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (data.error.value.isNotEmpty()) {
            Text(text = data.error.value, color = Color.Red, style = TextStyle(fontSize = 20.sp))
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        }
        Text(text = "Registro de Usuario", style = TextStyle(fontSize = 20.sp))
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        EmailField()
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        TextoPassword()
        ButtonRegisterAuth(viewModel, navController, auth)
    }
}

@Composable
fun ButtonRegisterAuth(viewModel: UserViewModel, navController: NavController, auth: FirebaseAuth) {
    Button(
        onClick = {
            data.error.value = ""
            viewModel.registerAuthUser(data.email.value, data.password.value, auth, navController)
        }
    ) {
        Text("Registrarse")
    }
}

@Composable
fun EmailField() {
    TextField(
        value = data.email.value,
        modifier = Modifier.fillMaxWidth(0.75f),
        onValueChange = {
            data.email.value = it
        },
        placeholder = {
            Text("Email")
        },
    )
}

@Composable
fun RegisterUserScreen(navController: NavController, viewModel: UserViewModel, auth: FirebaseAuth) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registro de Usuario", style = TextStyle(fontSize = 20.sp))
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        OutlinedTexts()
        AddUser(viewModel, navController, auth)
    }
}

@Composable
fun AddUser(viewModel: UserViewModel, navController: NavController, auth: FirebaseAuth) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Button(
            onClick = {
                viewModel.addUser(
                    User(auth.currentUser?.email.toString(), data.nombre.value, data.edad.value.toIntOrNull() ?: 0),
                    navController
                )
            }
        ) {
            Text("Añadir Usuario")
        }
    }
}

// Composable que contiene los campos de texto para el nombre y la edad
@Composable
fun OutlinedTexts(){
    OutlinedTextField(
        value = data.nombre.value,
        onValueChange = {
            data.nombre.value = it
        },
        label = {
            Text("Nombre")
        },
        modifier = Modifier.padding(16.dp)
    )
    OutlinedTextField(
        value = data.edad.value,
        onValueChange = {
            // Solo coge el valor si es un número
            data.edad.value = it.takeIf {
                    text -> text.all {
                it.isDigit()
            }
            } ?: data.edad.value
        },
        label = {
            Text("Edad")
        },
        modifier = Modifier.padding(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}
