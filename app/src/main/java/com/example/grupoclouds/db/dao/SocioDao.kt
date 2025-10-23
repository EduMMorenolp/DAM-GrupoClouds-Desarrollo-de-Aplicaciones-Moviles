package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("""
        SELECT
            p.nombre,
            p.apellido,
            p.dni,
            s.cuota_hasta
        FROM Socio AS s
        INNER JOIN Persona AS p ON s.id_persona = p.id_persona
        WHERE (p.nombre LIKE '%' || :query || '%' OR p.apellido LIKE '%' || :query || '%' OR p.dni LIKE '%' || :query || '%')
        AND s.cuota_hasta BETWEEN :fechaInicio AND :fechaFin
        ORDER BY s.cuota_hasta ASC
    """)
    suspend fun getSociosPorVencimientoYBusqueda(query: String, fechaInicio: String, fechaFin: String): List<SocioConDetalles>

    @Query("""
        SELECT
            p.nombre,
            p.apellido,
            p.dni,
            s.cuota_hasta
        FROM Socio AS s
        INNER JOIN Persona AS p ON s.id_persona = p.id_persona
        WHERE p.nombre LIKE '%' || :query || '%' OR p.apellido LIKE '%' || :query || '%' OR p.dni LIKE '%' || :query || '%'
        ORDER BY s.cuota_hasta ASC
    """)
    suspend fun getTodosLosSociosPorBusqueda(query: String): List<SocioConDetalles>

    @Query("UPDATE Socio SET cuota_hasta = :nuevaFecha WHERE id_socio = :socioId")
    suspend fun actualizarCuotaHasta(socioId: Int, nuevaFecha: String)

}
