package com.example.myapplication.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.myapplication.Composables.BottomBar

@Composable
fun LoginScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {BottomBar(navController)}
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)){
            var usuario by remember { mutableStateOf("") }
            var pass by remember { mutableStateOf("") }
            TextField(
                value = usuario,
                onValueChange = {usuario=it},
                label = {Text("usuario")}
            )
            TextField(
                value = pass,
                onValueChange = {pass=it},
                label = {Text("contraseña")}
            )
            Button(onClick = {
                println("le diste al boton $usuario $pass")
                navController.navigate("principal")
            }) {
                Text("Ingresar")
            }
        }
    }
}