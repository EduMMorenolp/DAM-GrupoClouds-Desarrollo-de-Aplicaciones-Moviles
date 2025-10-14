package com.example.grupoclouds.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "Cuota",
    foreignKeys = [
        ForeignKey(
            entity = Socio::class,
            parentColumns = ["id_socio"],
            childColumns = ["id_socio"],
            onDelete = ForeignKey.CASCADE // Si se borra un Socio, se borran sus cuotas asociadas.
        )
    ]
)
data class Cuota(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_cuota")
    val id: Int = 0,

    @ColumnInfo(name = "monto")
    val monto: Float, // Room maneja Float, que se corresponde con REAL en SQLite.

    @ColumnInfo(name = "fecha_pago")
    val fechaPago: String?,

    @ColumnInfo(name = "fecha_vence")
    val fechaVence: String?,

    @ColumnInfo(name = "medio_pago")
    val medioPago: String?,

    @ColumnInfo(name = "promocion")
    val promocion: Int?,

    // Clave foránea que referencia a la tabla Socio.
    @ColumnInfo(name = "id_socio", index = true)
    val idSocio: Int
)