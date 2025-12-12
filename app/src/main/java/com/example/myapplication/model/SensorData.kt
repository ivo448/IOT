package com.example.myapplication.model

data class SensorData(
    var valorVibracion: Int = 0,
    var alerta: Boolean = false
) {
    // Constructor
    constructor() : this(0, false)
}