package com.example.testfirebase.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.testfirebase.R
import com.example.testfirebase.data.data
import com.example.testfirebase.model.User
import com.example.testfirebase.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoggedScreen(navController: NavController, viewModel: UserViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                navController.navigate("login")
                viewModel.changeConnection(data.userConnectedID.value, false)
                FirebaseAuth.getInstance().signOut()
            }
        ) {
            Text("Cerrar sesión")
        }
        UserList(viewModel = viewModel, users = data.users.value)
        if (data.userConnected.value?.email=="damian@gmail.com"){
            EliminarUsuario(viewModel = viewModel)
        }
        ButtonModificarUsuario(viewModel = viewModel, navController)
    }
}

@Composable
fun ButtonModificarUsuario(viewModel: UserViewModel, navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                navController.navigate("changeMyUser")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Modificar usuario")
        }
    }
}

@Composable
fun EliminarUsuario(viewModel: UserViewModel) {
    Column (
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextField(
            value = data.email.value,
            onValueChange = { data.email.value = it },
            placeholder = { Text("Email") }
        )
        Button(
            onClick = {
                viewModel.deleteUser(data.email.value)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Eliminar usuario")
        }
    }
}


// To see the users
// Composable que contiene la lista de usuarios
@Composable
fun UserList(viewModel: UserViewModel, users: List<User>) {
    LazyColumn(
        modifier = if (data.userConnected.value?.email=="damian@gmail.com") Modifier.fillMaxHeight(0.75f).fillMaxWidth(1f)else Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Iterar sobre la lista de usuarios
        items(users) { user ->
            if(user.email != data.userConnected.value?.email){
                UserListItem(viewModel = viewModel, user = user)
            }
        }
    }
}

// Composable que contiene un elemento de la lista de usuarios
@Composable
fun UserListItem(viewModel: UserViewModel, user: User) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Column(

        ) {
            // Texto con el uid del usuario y si está conectado
            Row {
                Text(
                    text = "${user.email}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Icon(
                    painter = if (user.connected) painterResource(id = R.drawable.ic_connected) else painterResource(id = R.drawable.ic_disconnected),
                    contentDescription = if (user.connected) "Usuario conectado" else "Usuario desconectado",
                    tint = if (user.connected) Color.Green else Color.Red // Define el color del icono aquí
                )
            }
            Text(
                text = "Nombre: ${user.nombre}"
            )
            Text(text = "Edad: ${user.edad}")
        }
        Log.d("User", user.toString())
    }
}