package com.example.grupoclouds.db.model

import androidx.room.ColumnInfo

/**
 * Data class (modelo de vista) que no es una entidad de la base de datos.
 * Se utiliza para contener el resultado combinado de una consulta con JOIN
 * entre las tablas Socio y Persona, específicamente para la lista de socios vencidos.
 */
data class SocioConDetalles(
    // Usamos @ColumnInfo aquí para que Room sepa cómo mapear los resultados de la consulta
    // a los campos de esta clase, especialmente si los nombres de las columnas en la consulta
    // son diferentes a los nombres de las propiedades.

    @ColumnInfo(name = "nombre")
    val nombre: String,

    @ColumnInfo(name = "apellido")
    val apellido: String?,

    @ColumnInfo(name = "dni")
    val dni: String,

    @ColumnInfo(name = "cuota_hasta")
    val cuota_hasta: String
)
