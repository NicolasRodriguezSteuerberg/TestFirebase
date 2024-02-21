package com.example.testfirebase.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.testfirebase.data.data
import com.example.testfirebase.viewmodel.UserViewModel

@Composable
fun ChangeUserScreen(navController: NavController,viewModel: UserViewModel) {
    data.nombre.value = data.userConnected.value?.nombre.toString()
    data.edad.value = data.userConnected.value?.edad.toString()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        MyEmail()
        TextosToChange()
        ButtonsChange(navController, viewModel)
    }
}

@Composable
fun MyEmail() {
    Text(
        modifier = Modifier.fillMaxWidth(0.75f),
        text = "Mi email: ${data.userConnected.value?.email}",
    )
}

@Composable
fun TextosToChange() {
    TextField(
        value = data.nombre.value,
        onValueChange = { data.nombre.value = it },
        placeholder = { Text("Nuevo nombre ") }
    )
    TextField(
        value = data.edad.value,
        onValueChange = { data.edad.value = it },
        placeholder = { Text("Nueva edad") }
    )
}

@Composable
fun ButtonsChange(navController: NavController, viewModel: UserViewModel) {
    Column {
        // boton modificar usuario
        Button(
            onClick = {
                viewModel.changeUser(navController)
            }
        ) {
            Text(text = "Modificar usuario")
        }

        // cancelar
        Button(
            onClick = {
                navController.navigate("logged")
            }
        ) {
            Text(text = "Cancelar")
        }
    }
}