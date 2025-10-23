package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.grupoclouds.db.entity.Socio
import com.example.grupoclouds.db.model.SocioConDetalles
import com.example.grupoclouds.Miembro
import kotlinx.coroutines.flow.Flow

@Dao
interface SocioDao {

    @Insert
    suspend fun insertarSocio(socio: Socio): Long

    @Query("""
        SELECT s.* FROM Socio AS s
        INNER JOIN Persona AS p ON s.id_persona = p.id_persona
        WHERE p.dni = :dni
    """)
    suspend fun obtenerSocioPorDNI(dni: String): Socio?

    @Query("UPDATE Socio SET cuota_hasta = :nuevaFechaVencimiento WHERE id_socio = :idSocio")
    suspend fun actualizarFechaVencimiento(idSocio: Int, nuevaFechaVencimiento: String)

    @Transaction
    @Query("""
        SELECT
            p.nombre,
            p.apellido,
            p.dni,
            s.cuota_hasta
        FROM Socio AS s
        JOIN Persona AS p ON s.id_persona = p.id_persona
        WHERE s.cuota_hasta < :fechaActual
        ORDER BY s.cuota_hasta ASC
    """)
    fun obtenerSociosVencidos(fechaActual: String): Flow<List<SocioConDetalles>>

    @Transaction
    @Query("""
        SELECT
            p.nombre,
            p.apellido,
            p.dni,
            s.cuota_hasta
        FROM Socio AS s
        JOIN Persona AS p ON s.id_persona = p.id_persona
        WHERE s.cuota_hasta BETWEEN :fechaHoy AND :fechaLimite
        ORDER BY s.cuota_hasta ASC
    """)
    fun obtenerSociosPorVencer(fechaHoy: String, fechaLimite: String): Flow<List<SocioConDetalles>>

    @Query("SELECT * FROM Socio WHERE id_persona = :personaId")
    suspend fun obtenerSocioPorPersonaId(personaId: Int): Socio?

    // Nueva consulta para obtener todos los socios como objetos Miembro
    @Transaction
    @Query("""
        SELECT 
            CASE 
                WHEN p.apellido IS NOT NULL THEN p.nombre || ' ' || p.apellido 
                ELSE p.nombre 
            END as nombre,
            CAST(s.id_socio AS TEXT) as idSocio,
            'https://randomuser.me/api/portraits/men/32.jpg' as urlImagen
        FROM Socio s
        INNER JOIN Persona p ON s.id_persona = p.id_persona
        ORDER BY p.nombre, p.apellido
    """)
    suspend fun obtenerTodosSociosComoMiembros(): List<Miembro>
}
