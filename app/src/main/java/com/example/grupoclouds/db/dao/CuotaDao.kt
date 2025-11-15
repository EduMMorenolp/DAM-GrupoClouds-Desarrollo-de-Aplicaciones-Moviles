package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.grupoclouds.db.entity.Cuota
import com.example.grupoclouds.db.model.DetalleHistorialPago
import kotlinx.coroutines.flow.Flow

@Dao
interface CuotaDao {

    @Insert
    suspend fun insertarCuota(cuota: Cuota): Long

    /**
     * Obtiene el historial completo de todos los pagos realizados, uniendo la información
     * de las tablas Cuota, Socio y Persona.
     * Se usan alias de columna (AS) para que los nombres de las columnas del resultado
     * coincidan exactamente con los nombres de las propiedades de la data class DetalleHistorialPago.
     */
    @Transaction
    @Query("""
        SELECT 
            p.nombre AS nombre,
            p.apellido AS apellido,
            c.monto AS monto,
            c.fecha_pago AS fechaPago,
            c.fecha_vence AS fechaVence,
            c.tipo_pago AS tipoPago,
            c.metodo_pago AS metodoPago
        FROM Cuota AS c
        INNER JOIN Socio AS s ON c.id_socio = s.id_socio
        INNER JOIN Persona AS p ON s.id_persona = p.id_persona
        ORDER BY c.fecha_pago DESC
    """)
    fun obtenerHistorialPagos(): Flow<List<DetalleHistorialPago>>
}
