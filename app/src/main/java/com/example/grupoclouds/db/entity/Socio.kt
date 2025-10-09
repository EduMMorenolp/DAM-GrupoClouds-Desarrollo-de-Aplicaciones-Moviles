package com.example.grupoclouds.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Socio",
    foreignKeys = [
        ForeignKey(
            entity = Persona::class,
            parentColumns = ["id_persona"],
            childColumns = ["id_persona"],
            onDelete = ForeignKey.CASCADE // si borras una Persona, se borra el Socio asociado
        )
    ]
)
data class Socio(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_socio")
    val id: Int = 0,

    @ColumnInfo(name = "fecha_alta")
    val fechaAlta: String?,

    @ColumnInfo(name = "cuota_hasta")
    val cuotaHasta: String?,

    @ColumnInfo(name = "tiene_carnet")
    val tieneCarnet: Boolean, // Room maneja Booleanos automáticamente

    @ColumnInfo(name = "ficha_medica")
    val fichaMedica: Boolean,

    @ColumnInfo(name = "id_persona", index = true) //
    val idPersona: Int
)