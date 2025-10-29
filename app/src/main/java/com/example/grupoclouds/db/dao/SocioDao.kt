package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.grupoclouds.db.entity.Socio
import com.example.grupoclouds.db.model.SocioConDetalles

@Dao
interface SocioDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarSocio(socio: Socio): Long

    @Query("UPDATE Socio SET cuota_hasta = :nuevaFechaVencimiento WHERE id_socio = :idSocio")
    suspend fun actualizarFechaVencimiento(idSocio: Int, nuevaFechaVencimiento: String)


    @Query("SELECT * FROM Socio WHERE id_persona = :personaId")
    suspend fun obtenerSocioPorPersonaId(personaId: Int): Socio?

    @Query("""
        SELECT s.* FROM Socio AS s
        INNER JOIN Persona AS p ON s.id_persona = p.id_persona
        WHERE p.dni = :dni
    """)
    suspend fun obtenerSocioPorDNI(dni: String): Socio?


    @Query("UPDATE Socio SET tiene_carnet = :tieneCarnet WHERE id_socio = :idSocio")
    suspend fun actualizarEstadoCarnet(idSocio: Int, tieneCarnet: Boolean)

    @Transaction
    @Query("""
        SELECT
            p.nombre,
            p.apellido,
            p.dni,
            s.cuota_hasta
        FROM Socio AS s
        INNER JOIN Persona AS p ON s.id_persona = p.id_persona
        WHERE s.cuota_hasta BETWEEN :fechaInicio AND :fechaFin
        AND (
            CASE 
                WHEN :query = '' THEN 1
                ELSE (p.nombre LIKE '%' || :query || '%' OR p.apellido LIKE '%' || :query || '%' OR p.dni LIKE '%' || :query || '%')
            END
        )
        ORDER BY s.cuota_hasta ASC
    """)
    suspend fun getSociosPorVencimientoYBusqueda(query: String, fechaInicio: String, fechaFin: String): List<SocioConDetalles>

    @Transaction
    @Query("""
        SELECT
            p.nombre,
            p.apellido,
            p.dni,
            s.cuota_hasta
        FROM Socio AS s
        INNER JOIN Persona AS p ON s.id_persona = p.id_persona
        WHERE 
            CASE 
                WHEN :query = '' THEN 1
                ELSE (p.nombre LIKE '%' || :query || '%' OR p.apellido LIKE '%' || :query || '%' OR p.dni LIKE '%' || :query || '%')
            END
        ORDER BY s.cuota_hasta ASC
    """)
    suspend fun getTodosLosSociosPorBusqueda(query: String): List<SocioConDetalles>

    @Query("UPDATE Socio SET cuota_hasta = :nuevaFecha WHERE id_socio = :socioId")
    suspend fun actualizarCuotaHasta(socioId: Int, nuevaFecha: String)
}
