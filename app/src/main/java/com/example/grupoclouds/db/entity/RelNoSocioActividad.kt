package com.example.grupoclouds.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "NoSocio_Actividad",
    primaryKeys = ["id_no_socio", "id_actividad"], // Clave primaria compuesta
    foreignKeys = [
        ForeignKey(
            entity = NoSocio::class,
            parentColumns = ["id_no_socio"],
            childColumns = ["id_no_socio"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Actividad::class,
            parentColumns = ["id_actividad"],
            childColumns = ["id_actividad"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RelNoSocioActividad(
    @ColumnInfo(name = "id_no_socio")
    val idNoSocio: Int,

    @ColumnInfo(name = "id_actividad", index = true) // 'index = true' es bueno aquí
    val idActividad: Int
)