package com.example.grupoclouds.db.model

/**
 * Modelo completo para mostrar toda la información de un miembro
 */
data class MiembroCompleto(
    val nombre: String,
    val apellido: String?,
    val dni: String,
    val fechaNacimiento: String?,
    val esSocio: Boolean,
    val idSocio: String?, // ID del socio o DNI si es no-socio
    val fechaAlta: String? = null, // Solo para socios
    val cuotaHasta: String? = null, // Solo para socios
    val tieneCarnet: Boolean = false // Solo para socios
) {
    // Función para obtener el nombre completo
    fun nombreCompleto(): String {
        return if (apellido != null) "$nombre $apellido" else nombre
    }

    // Función para obtener información del estado (para socios)
    fun getEstadoInfo(): String? {
        return when {
            esSocio && cuotaHasta != null -> "Cuota: $cuotaHasta"
            esSocio && tieneCarnet -> "Con carnet"
            esSocio -> "Sin carnet"
            else -> null
        }
    }

    // Función para formatear fecha de nacimiento
    fun getFechaNacimientoFormateada(): String? {
        return fechaNacimiento?.let { "Nacido: $it" }
    }
}
