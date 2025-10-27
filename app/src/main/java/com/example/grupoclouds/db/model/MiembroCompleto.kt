package com.example.grupoclouds.db.model

/**
 * Modelo completo para mostrar toda la información de un miembro
 */
data class MiembroCompleto(
    val nombre: String,
    val apellido: String?,
    val dni: String,
    val email: String?,
    val fechaNacimiento: String?,
    val esSocio: Boolean,
    val idSocio: String?, // ID del socio o DNI si es no-socio
    val fechaAlta: String? = null, // Solo para socios
    val cuotaHasta: String? = null, // Solo para socios
    val tieneCarnet: Boolean = false, // Solo para socios
    val fichaMedica: Boolean = false // Campo para indicar si trajo ficha médica
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

    // Función para obtener el estado de la ficha médica
    fun getEstadoFichaMedica(): String {
        return if (fichaMedica) "Ficha médica" else "Sin ficha médica"
    }

    // Función para obtener el estado del carnet (solo para socios)
    fun getEstadoCarnet(): String? {
        return if (esSocio) {
            if (tieneCarnet) "Con carnet" else "Sin carnet"
        } else {
            null // Los no-socios no tienen carnet
        }
    }

    // Función para obtener el color del estado del carnet
    fun getColorCarnet(): String {
        return if (tieneCarnet) "#4CAF50" else "#FF9800" // Verde si tiene, naranja si no tiene
    }
}
