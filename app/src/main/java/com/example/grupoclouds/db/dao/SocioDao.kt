package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grupoclouds.db.entity.Socio
import com.example.grupoclouds.db.model.SocioConDetalles
import kotlinx.coroutines.flow.Flow

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

    @Query("UPDATE Socio SET cuota_hasta = :nuevaFechaVencimiento WHERE id_socio = :idSocio")
    suspend fun actualizarFechaVencimiento(idSocio: Int, nuevaFechaVencimiento: String)

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
