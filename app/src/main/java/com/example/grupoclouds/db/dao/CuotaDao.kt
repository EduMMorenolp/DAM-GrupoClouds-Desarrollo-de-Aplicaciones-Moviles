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
     *
     * @return Un Flow que emite la lista de detalles de pago. Room se encargará de
     * re-ejecutar la consulta y emitir una nueva lista cada vez que los datos de las
     * tablas subyacentes cambien.
     */
    @Transaction
    @Query("""
        SELECT 
            p.nombre, 
            p.apellido, 
            c.monto, 
            c.fechaPago, 
            c.fechaVence, 
            c.tipoPago, 
            c.metodoPago
        FROM Cuota AS c
        INNER JOIN Socio AS s ON c.id_socio = s.id_socio
        INNER JOIN Persona AS p ON s.id_persona_socio = p.id_persona
        ORDER BY c.fechaPago DESC
    """)
    fun obtenerHistorialPagos(): Flow<List<DetalleHistorialPago>>
}