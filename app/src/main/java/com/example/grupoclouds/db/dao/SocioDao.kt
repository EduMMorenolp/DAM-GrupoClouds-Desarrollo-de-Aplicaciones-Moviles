package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.grupoclouds.db.entity.Socio
import com.example.grupoclouds.db.model.SocioConDetalles
import kotlinx.coroutines.flow.Flow

@Dao
interface SocioDao {

    @Insert
    suspend fun insertarSocio(socio: Socio): Long

    @Query("SELECT s.* FROM Socio AS s INNER JOIN Persona AS p ON s.id_persona_socio = p.id_persona WHERE p.dni = :dni")
    suspend fun obtenerSocioPorDNI(dni: String): Socio?

    @Query("UPDATE Socio SET cuota_hasta = :nuevaFechaVencimiento WHERE id_socio = :idSocio")
    suspend fun actualizarFechaVencimiento(idSocio: Int, nuevaFechaVencimiento: String)

    @Transaction
    @Query("SELECT p.nombre, p.apellido, p.dni, s.cuota_hasta FROM Socio AS s JOIN Persona AS p ON s.id_persona_socio = p.id_persona WHERE s.cuota_hasta < :fechaActual ORDER BY s.cuota_hasta ASC")
    fun obtenerSociosVencidos(fechaActual: String): Flow<List<SocioConDetalles>>

    /**
     * 7.3 y 7.4: Obtiene una lista de socios cuya cuota está por vencer en un rango de fechas determinado.
     * Utiliza la cláusula BETWEEN para filtrar las fechas y un JOIN para combinar los datos.
     *
     * @param fechaHoy La fecha de inicio del rango (hoy).
     * @param fechaLimite La fecha de fin del rango (hoy + 7 días).
     * @return Un Flow que emite la lista de socios por vencer, ordenados por fecha de vencimiento.
     */
    @Transaction
    @Query("""
        SELECT
            p.nombre,
            p.apellido,
            p.dni,
            s.cuota_hasta
        FROM Socio AS s
        JOIN Persona AS p ON s.id_persona_socio = p.id_persona
        WHERE s.cuota_hasta BETWEEN :fechaHoy AND :fechaLimite
        ORDER BY s.cuota_hasta ASC
    """)
    fun obtenerSociosPorVencer(fechaHoy: String, fechaLimite: String): Flow<List<SocioConDetalles>>
}
