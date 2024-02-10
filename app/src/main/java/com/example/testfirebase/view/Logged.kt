package com.example.testfirebase.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                navController.navigate("registerAuth")
                viewModel.changeConnection(data.userConnectedID.value, false)
                FirebaseAuth.getInstance().signOut()
            }
        ) {
            Text("Cerrar sesión")
        }
        UserList(viewModel = viewModel, users = data.users.value)
    }
}
// To see the users
// Composable que contiene la lista de usuarios
@Composable
fun UserList(viewModel: UserViewModel, users: List<User>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Iterar sobre la lista de usuarios
        items(users) { user ->
            UserListItem(viewModel = viewModel, user = user)
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
            .clickable(
                onClick = {
                // Obtener el id del usuario
                viewModel.getIdUser(user)
                data.nombre.value = user.nombre
                data.edad.value = user.edad.toString()
                }
            )
    ) {
        Column(

        ) {
            // Texto con el uid del usuario y si está conectado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                Text(
                    text = "ID: ${viewModel.getIdUser(user)}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Icon(
                    painter = if (user.connected) painterResource(id = R.drawable.ic_connected) else painterResource(id = R.drawable.ic_disconnected),
                    contentDescription = if (user.connected) "Usuario conectado" else "Usuario desconectado"
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