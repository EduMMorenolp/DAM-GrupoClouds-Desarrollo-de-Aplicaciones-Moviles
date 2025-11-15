package com.example.grupoclouds

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Define la estructura de la tabla 'Actividad' en la base de datos.
 */
@Entity(tableName = "Actividad")
data class Actividad(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_actividad")
    val id: Int = 0,

    @ColumnInfo(name = "nombre")
    val nombre: String,

    @ColumnInfo(name = "instructor")
    val instructor: String,

    @ColumnInfo(name = "precio")
    val precio: String // Para futuras mejoras, considera usar un tipo numérico como Double
)
