package com.example.myapplication.screens

import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.SeguridadViewModel

@Composable
fun DashboardScreen(
    // Inyectamos el ViewModel
    viewModel: SeguridadViewModel = viewModel()
) {
    // Obtenemos los estados directamente del ViewModel
    val sensor = viewModel.estadoSensor
    val sistema = viewModel.estadoSistema

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. VISUALIZACIÓN
        Text("Panel de Seguridad", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        // Tarjeta de Estado
        if (sensor.alerta && sistema.armado) {
            Card(colors = CardDefaults.cardColors(containerColor = Color.Red)) {
                Text("ALERTA DE INTRUSO", color = Color.White, modifier = Modifier.padding(16.dp))
            }
        } else {
            Card(colors = CardDefaults.cardColors(containerColor = Color.Green)) {
                Text("SISTEMA SEGURO", color = Color.Black, modifier = Modifier.padding(16.dp))
            }
        }

        Text("Vibración: ${sensor.valorVibracion}")

        Spacer(modifier = Modifier.height(30.dp))

        // 2. CONTROLES MANUALES
        Text("Control de Acceso")

        // Switch Armado
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Armado de Alarma: ")
            Switch(
                checked = sistema.armado,
                onCheckedChange = { viewModel.toggleArmado(it) } // Llamamos al ViewModel
            )
        }

        // Botón Servo
        Text("Servo Motor")
        Button(onClick = {
            viewModel.toggleServo()
        }) {
            Text(if (sistema.servoAbierto) "ABIERTO" else "CERRADO")
        }

        // Switch LED
        Text("LED")
        Switch(
            checked = sistema.ledEncendido,
            onCheckedChange = {
                viewModel.toggleLed(it)
            }
        )
    }
}