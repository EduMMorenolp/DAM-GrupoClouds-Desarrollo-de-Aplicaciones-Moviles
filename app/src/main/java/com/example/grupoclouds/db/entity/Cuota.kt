package com.example.grupoclouds.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad que representa un registro de pago (una cuota) en la base de datos.
 * Cada cuota está vinculada a un Socio a través de una clave foránea.
 */
@Entity(
    tableName = "Cuota",
    // Define la relación entre la tabla Cuota y la tabla Socio.
    foreignKeys = [
        ForeignKey(
            entity = Socio::class, // La tabla "padre" a la que nos vinculamos.
            parentColumns = ["id_socio"], // La columna de la tabla padre (Socio).
            childColumns = ["id_socio"], // La columna de esta tabla (Cuota) que guarda la referencia.
            onDelete = ForeignKey.RESTRICT // Regla: No se puede borrar un socio si tiene cuotas asociadas.
        )
    ],
    // Crear un índice en la columna de la clave foránea mejora el rendimiento de las consultas.
    indices = [Index(value = ["id_socio"])]
)
data class Cuota(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_cuota")
    val id: Int = 0,

    // Esta es la columna que almacena el ID del socio que pagó.
    @ColumnInfo(name = "id_socio")
    val idSocio: Int,

    @ColumnInfo(name = "monto")
    val monto: Float,

    @ColumnInfo(name = "fecha_pago")
    val fechaPago: String,

    @ColumnInfo(name = "fecha_vence")
    val fechaVence: String,

    // Describe el plan, ej: "Plan 15 Días", "Plan Mensual"
    @ColumnInfo(name = "tipo_pago")
    val tipoPago: String,

    // Describe el método, ej: "Efectivo", "Tarjeta"
    @ColumnInfo(name = "metodo_pago")
    val metodoPago: String
)
