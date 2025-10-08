package com.example.grupoclouds.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "Administrador",
    indices = [Index(value = ["nombre_usuario"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = Persona::class,
            parentColumns = ["id_persona"],
            childColumns = ["id_persona"],
            onDelete = ForeignKey.CASCADE // si borras una Persona, se borra el Administrador asociado
        )
    ]
)
data class Administrador(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_admin")
    val id: Int = 0,

    @ColumnInfo(name = "nombre_usuario")
    val nombreUsuario: String,

    @ColumnInfo(name = "contrasena")
    val contrasena: String?,

    @ColumnInfo(name = "fecha_registro")
    val fechaRegistro: String?,

    @ColumnInfo(name = "id_persona", index = true) //
    val idPersona: Int
)