package com.example.grupoclouds.db.model

/**
 * Data class que no representa una tabla en la base de datos (@Entity).
 * Su propósito es servir como un modelo de datos personalizado para la UI.
 * Se construye a partir de una consulta con JOINs para mostrar una lista del historial de pagos.
 */
data class DetalleHistorialPago(
    // Datos de la Persona
    val nombre: String,
    val apellido: String?,

    // Datos de la Cuota
    val monto: Float,
    val fechaPago: String,
    val fechaVence: String,
    val tipoPago: String,
    val metodoPago: String
)
