package com.example.testfirebase.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import com.example.testfirebase.R
import com.example.testfirebase.data.data
import com.example.testfirebase.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController, viewModel: UserViewModel, auth: FirebaseAuth){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextsLogins()
        Buttons(navController, viewModel, auth)
    }
}
@Composable
fun Buttons(navController: NavController, viewModel: UserViewModel, auth: FirebaseAuth) {
    Row(
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalArrangement = Arrangement.Center
    ) {
        // boton login
        Button(
            onClick = {
                viewModel.login(data.email.value, data.password.value, auth,navController)
            }
        ) {
            Text(text = "Login")
        }

        // boton register
        Button(
            onClick = {
                navController.navigate("registerAuth")
            }
        ) {
            Text(text = "Register")
        }
    }
}

@Composable
fun TextsLogins() {
    Column(
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // texto email
        TextField(
            value = data.email.value,
            onValueChange = {
                data.email.value = it
            },
            placeholder = {
                Text("Email")
            }
        )

        Spacer(modifier = Modifier.fillMaxSize(0.01f))

        // texto password
        TextoPassword()
    }
}

@Composable
fun TextoPassword() {
    TextField(
        value = data.password.value,
        onValueChange = {
            data.password.value = it
        },
        placeholder = {
            Text("Password")
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        visualTransformation = if (data.passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(
                onClick = {
                    data.passwordVisible.value = !data.passwordVisible.value
                }
            ) {
                Icon(
                    painter = painterResource(
                        id = if (data.passwordVisible.value)
                            R.drawable.ic_visibility
                        else
                            R.drawable.ic_visibility_off
                    ),
                    contentDescription = "visibility icon"
                )
            }
        }
    )
}
