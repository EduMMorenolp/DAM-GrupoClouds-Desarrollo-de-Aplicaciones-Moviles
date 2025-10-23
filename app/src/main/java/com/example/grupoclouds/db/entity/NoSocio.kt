package com.example.grupoclouds.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "NoSocio",
    foreignKeys = [
        ForeignKey(
            entity = Persona::class,
            parentColumns = ["id_persona"],
            childColumns = ["id_persona"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NoSocio(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_no_socio")
    val id: Int = 0,

    @ColumnInfo(name = "id_persona")
    val idPersona: Int
)
