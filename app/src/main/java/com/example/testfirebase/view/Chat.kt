package com.example.testfirebase.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.testfirebase.R
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
                    Text(text = emailDestinatario)
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("logged")
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.zIndex(1f) // para que la top
            )
        },
    ){ contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
                .fillMaxHeight(),
        ){
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(end = 16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f)
            ){
                items(data.messagesList.value){message ->
                    Messages(message = message.mensaje, destinatario = message.destinatario)
                }
            }

            ChatInput(
                modifier = Modifier
                    .fillMaxHeight(0.11f)
                    .align(Alignment.End),
                viewModel = viewModel,
                emailDestinatario = emailDestinatario,
                emailEmisor = emailEmisor
            )

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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInput(
    modifier: Modifier,
    viewModel: ChatViewModel,
    emailDestinatario: String,
    emailEmisor: String
) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        OutlinedTextField(
            value = data.messageSend.value,
            onValueChange = { data.messageSend.value = it },
            placeholder = {
                Text(stringResource(R.string.message))
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 8.dp)
                .background(Color(0xFF28447C), RoundedCornerShape(16.dp)),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),

            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            maxLines = 6

        )
        IconButton(
            onClick = {
                if(data.messageSend.value!="") {
                    viewModel.addMessage(emailDestinatario, emailEmisor, data.messageSend.value)
                    data.messageSend.value = ""
                }
            },
            modifier = Modifier
                .background(Color(0xFF25D366), RoundedCornerShape(50))
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send message"
            )
        }
    }
}