package com.example.testfirebase.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.testfirebase.data.data
import com.example.testfirebase.viewmodel.ChatViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController, viewModel: ChatViewModel,
    emailDestinatario: String, emailEmisor: String
) {
    viewModel.listenForMessages(emailDestinatario, emailEmisor)
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding()),
            ){
                ChatText(
                    modifier = Modifier
                        .fillMaxHeight(0.9f)
                        .align(Alignment.TopCenter),
                    emailDestinatario,
                    emailEmisor
                )
                ChatInput(
                    modifier = Modifier
                        .fillMaxHeight(0.1f)
                        .align(Alignment.BottomCenter),
                    viewModel = viewModel,
                    emailDestinatario = emailDestinatario,
                    emailEmisor = emailEmisor
                )

            }
        }
    )
}

@Composable
fun ChatText(modifier: Modifier,emailDestinatario: String, emailEmisor: String) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ){
        items(data.messagesList.value){message ->
            Messages(message = message.mensaje, destinatario = message.destinatario)
        }
    }
}

@Composable
fun Messages(message: String, destinatario: String) {
    val backgroudColor = if (destinatario == data.userConnected.value?.email.toString())
            Color(0xFF2F312E)
        else
            Color(0xFF3A7023)
    Row (
        modifier = Modifier.fillMaxWidth(1f),
        horizontalArrangement = if (destinatario == data.userConnected.value?.email.toString()) Arrangement.Start else Arrangement.End
    ){
        Text(
            text = message,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .background(backgroudColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp),
            textAlign = if (destinatario == data.userConnected.value?.email.toString()) TextAlign.Start else TextAlign.End,
        )
    }
    Spacer(modifier = Modifier.padding(top = 8.dp))
}

@Composable
fun ChatInput(
    modifier: Modifier,
    viewModel: ChatViewModel,
    emailDestinatario: String,
    emailEmisor: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = data.messageSend.value,
            onValueChange = { data.messageSend.value = it },
        )
        Button(onClick = {
            viewModel.addMessage(emailDestinatario, emailEmisor, data.messageSend.value)
            data.messageSend.value = ""
        }) {
            Text(text = "Enviar")
        }
    }
}