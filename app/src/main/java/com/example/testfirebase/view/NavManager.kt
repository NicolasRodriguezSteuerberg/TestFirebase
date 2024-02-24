package com.example.testfirebase.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.testfirebase.data.data
import com.example.testfirebase.viewmodel.ChatViewModel
import com.example.testfirebase.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Nav(
    viewModel: UserViewModel,
    chatViewModel: ChatViewModel,
    auth: FirebaseAuth
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if(auth.currentUser != null) "logged" else "login"
    ){
        composable("login"){
            resetDataLogins()
            LoginScreen(navController, viewModel, auth)
        }
        composable("registerAuth"){
            resetDataLogins()
            RegisterAuthScreen(navController, viewModel, auth)
        }
        composable("registerUser"){
            RegisterUserScreen(navController, viewModel, auth)
        }
        composable("logged"){
            data.passwordVisible.value = false
            LoggedScreen(navController, viewModel)
        }
        composable("changeMyUser"){
            ChangeUserScreen(navController, viewModel)
        }
        /*
        composable(
            route = "chat/{destinatario}/emisor}",
        ){
            val destinatario = it.arguments?.getString("destinatario")
            val emisor = it.arguments?.getString("emisor")
            data.messagesList.value = emptyList()
            chatViewModel.listenForMessages(destinatario!!, emisor!!)
            ChatScreen(navController, chatViewModel, data.userChatting.value, data.userConnected.value?.email.toString())
        }*/
        composable(route="chat"){
            ChatScreen(navController, chatViewModel, data.userChatting.value, data.userConnected.value?.email.toString())
        }
    }
}

fun resetDataLogins(){
    data.passwordVisible.value = false
    data.email.value = ""
    data.password.value = ""
}