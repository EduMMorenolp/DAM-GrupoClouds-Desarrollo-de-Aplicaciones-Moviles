package com.example.grupoclouds.db.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Persona",
    indices = [Index(value = ["dni"], unique = true)]
)
data class Persona(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_persona")
    val id: Int = 0,

    @ColumnInfo(name = "nombre")
    val nombre: String,

    @ColumnInfo(name = "apellido")
    val apellido: String?,

    @ColumnInfo(name = "dni")
    val dni: String,

    @ColumnInfo(name = "fecha_nacimiento")
    val fechaNacimiento: String?
)
