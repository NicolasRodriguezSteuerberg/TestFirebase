package com.example.testfirebase.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testfirebase.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Nav(
    viewModel: UserViewModel,
    auth: FirebaseAuth
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if(auth.currentUser != null) "logged" else "registerAuth"
    ){
        composable("login"){
            LoginScreen(navController, viewModel)
        }
        composable("registerAuth"){
            RegisterAuthScreen(navController, viewModel, auth)
        }
        composable("registerUser"){
            RegisterUserScreen(navController, viewModel)
        }
        composable("logged"){
            LoggedScreen(navController, viewModel)
        }
    }
}