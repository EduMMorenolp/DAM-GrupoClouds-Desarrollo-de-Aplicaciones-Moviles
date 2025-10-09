package com.example.grupoclouds.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Actividad",)

data class Actividad(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_actividad")
    val id: Int = 0,

    @ColumnInfo(name = "nombre_actividad")
    val nombreActividad: String,

    @ColumnInfo(name = "tipo_actividad")
    val tipoActividad: String,

    @ColumnInfo(name = "profesor_actividad")
    val profesorActividad: String,

    @ColumnInfo(name = "horario_actividad")
    val horarioActividad: String,

    @ColumnInfo(name = "capacidad_actividad")
    val capacidadActividad: Int,

    @ColumnInfo(name = "costo_actividad")
    val costoActividad: Float,
)