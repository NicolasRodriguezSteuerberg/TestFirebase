package com.example.testfirebase.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.testfirebase.data.data
import com.example.testfirebase.viewmodel.ChatViewModel
import com.example.testfirebase.viewmodel.UserViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController, viewModel: ChatViewModel,
    emailDestinatario: String, emailEmisor: String
) {
    Log.d("ChatScreen", "emailDestinatario: $emailDestinatario, emailEmisor: $emailEmisor")
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Chat con $emailDestinatario")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("logged")
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
            ){
                ChatText(
                    Modifier
                        .fillMaxSize(0.9f)
                        .align(Alignment.TopStart),
                    emailDestinatario,
                    emailEmisor
                )

            }
        }
    )
}

@Composable
fun ChatText(modifier: Modifier, emailDestinatario: String, emailEmisor: String) {
    LazyColumn(
    ){
        items(data.messagesList.value){message ->
            Messages(message = message.mensaje, destinatario = message.destinatario)
        }
    }
}

@Composable
fun Messages(message: String, destinatario: String) {
    Row (
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalArrangement = if (destinatario == data.userConnected.value?.email.toString()) Arrangement.End else Arrangement.Start
    ){
        Text(
            text = message,
            modifier = Modifier
                .fillMaxWidth(0.75f),
            )
    }
}

@Composable
fun ChatInput(
    modifier: Modifier,
    viewModel: UserViewModel,
    emailDestinatario: String,
    emailEmisor: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Chat")
    }
}