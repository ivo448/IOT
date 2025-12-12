package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

// Importaciones de Jetpack Compose (para el estado de la UI)
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Importaciones de Firebase Realtime Database
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

// Importaciones de tus Modelos de Datos (Asegúrate de haber creado estos archivos en la carpeta 'model')
import com.example.myapplication.model.EstadoSistema
import com.example.myapplication.model.SensorData

class SeguridadViewModel : ViewModel() {

    // --- ESTADOS DE LA UI ---
    // Usamos mutableStateOf para que Compose sepa cuándo redibujar la pantalla.

    // Estado de lectura (lo que dice el sensor)
    var estadoSensor by mutableStateOf(SensorData())
        private set // El 'setter' es privado para que solo este ViewModel pueda modificarlo

    // Estado de escritura/lectura (lo que dicen los controles)
    var estadoSistema by mutableStateOf(EstadoSistema())
        private set

    // --- FIREBASE ---
    // Instanciamos la base de datos y apuntamos a los nodos principales
    private val db = Firebase.database
    private val refSensor = db.getReference("sensor")   // Nodo para datos del acelerómetro
    private val refSistema = db.getReference("sistema") // Nodo para actuadores (servo, led, armado)
    private val refLogs = db.getReference("logs")       // Nodo para el historial

    // --- INICIALIZACIÓN ---
    init {
        // Apenas se crea el ViewModel, empezamos a escuchar cambios en la nube
        escucharSensor()
        escucharSistema()
    }

    // --- FUNCIONES DE LECTURA (Escuchar) ---

    private fun escucharSensor() {
        refSensor.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Convertimos el JSON de Firebase a tu objeto Kotlin 'SensorData'
                val datos = snapshot.getValue(SensorData::class.java)

                if (datos != null) {
                    estadoSensor = datos

                    // Si el sensor detecta alerta Y el sistema está armado -> Guardamos Log
                    if (estadoSensor.alerta && estadoSistema.armado) {
                        registrarLog("¡ALERTA! Vibración detectada: ${estadoSensor.valorVibracion}")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error leyendo sensor: ${error.message}")
            }
        })
    }

    private fun escucharSistema() {
        refSistema.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Convertimos el JSON a objeto 'EstadoSistema'
                val estado = snapshot.getValue(EstadoSistema::class.java)
                if (estado != null) {
                    estadoSistema = estado
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error leyendo sistema: ${error.message}")
            }
        })
    }

    // --- FUNCIONES DE ESCRITURA (Controlar) ---
    // Estas funciones son llamadas desde los botones de la UI

    fun toggleServo() {
        Log.d("MI_APP", "Botón Servo presionado")
        // Invertimos el valor actual (true <-> false)
        val nuevoValor = !estadoSistema.servoAbierto

        // Escribimos solo en el campo específico 'servoAbierto'
        refSistema.child("servoAbierto").setValue(nuevoValor)

        val accion = if(nuevoValor) "Puerta Abierta" else "Puerta Cerrada"
        registrarLog("Usuario cambió servo: $accion")
    }

    fun toggleArmado(armado: Boolean) {
        Log.d("MI_APP", "Switch Armado cambiado a: $armado")
        refSistema.child("armado").setValue(armado)

        val mensaje = if(armado) "Sistema ARMADO" else "Sistema DESARMADO"
        registrarLog(mensaje)
    }

    fun toggleLed(encendido: Boolean) {
        refSistema.child("ledEncendido").setValue(encendido)
    }

    // --- FUNCIÓN DE LOGS (Historial) ---

    private fun registrarLog(mensaje: String) {
        // push() genera una clave única basada en el tiempo (ej: -Nj7x...)
        // Esto permite crear una lista en lugar de sobrescribir el dato
        val key = refLogs.push().key

        if (key != null) {
            val logData = mapOf(
                "fecha" to System.currentTimeMillis(), // Marca de tiempo (timestamp)
                "evento" to mensaje
            )
            refLogs.child(key).setValue(logData)
        }
    }
}