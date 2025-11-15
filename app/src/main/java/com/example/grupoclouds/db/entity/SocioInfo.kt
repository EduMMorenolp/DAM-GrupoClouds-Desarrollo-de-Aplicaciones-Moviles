package com.example.grupoclouds.db.entity

/**
 * Una clase de datos simple (POJO) para contener los resultados
 * de la consulta JOIN entre Socio y Persona.
 * ¡NO es una entidad de la base de datos!
 */
data class SocioInfo(
    // Campos que necesitamos de Persona
    val nombre: String,
    val apellido: String,
    val dni: String,

    // Campo que necesitamos de Socio
    val cuota_hasta: String
)
