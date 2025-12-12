package com.example.myapplication.model

data class EstadoSistema(
    // Estado del switch de "Armar Alarma"
    var armado: Boolean = false,

    // Estado del Servo
    var servoAbierto: Boolean = false,

    // Control manual del LED
    var ledEncendido: Boolean = false
) {
    // Constructor
    constructor() : this(false, false, false)
}