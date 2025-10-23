package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.grupoclouds.db.entity.Persona
import com.example.grupoclouds.Miembro

@Dao
interface PersonaDao {

    @Insert
    suspend fun insertarPersona(persona: Persona): Long

    @Query("SELECT * FROM Persona WHERE dni = :dni")
    suspend fun obtenerPersonaPorDNI(dni: String): Persona?

    // Nueva consulta para obtener todas las personas como miembros
    @Transaction
    @Query("""
        SELECT 
            CASE 
                WHEN p.apellido IS NOT NULL THEN p.nombre || ' ' || p.apellido 
                ELSE p.nombre 
            END as nombre,
            CASE 
                WHEN s.id_socio IS NOT NULL THEN CAST(s.id_socio AS TEXT)
                ELSE p.dni 
            END as idSocio,
            CASE 
                WHEN s.id_socio IS NOT NULL THEN 'https://randomuser.me/api/portraits/men/32.jpg'
                ELSE 'https://randomuser.me/api/portraits/women/44.jpg'
            END as urlImagen
        FROM Persona p
        LEFT JOIN Socio s ON p.id_persona = s.id_persona
        ORDER BY p.nombre, p.apellido
    """)
    suspend fun obtenerTodasLasPersonasComoMiembros(): List<Miembro>

    // Consulta para obtener solo los socios
    @Transaction
    @Query("""
        SELECT 
            CASE 
                WHEN p.apellido IS NOT NULL THEN p.nombre || ' ' || p.apellido 
                ELSE p.nombre 
            END as nombre,
            CAST(s.id_socio AS TEXT) as idSocio,
            'https://randomuser.me/api/portraits/men/32.jpg' as urlImagen
        FROM Persona p
        INNER JOIN Socio s ON p.id_persona = s.id_persona
        ORDER BY p.nombre, p.apellido
    """)
    suspend fun obtenerSociosComoMiembros(): List<Miembro>

    // Consulta para obtener solo los no-socios (personas sin registro en tabla Socio)
    @Transaction
    @Query("""
        SELECT 
            CASE 
                WHEN p.apellido IS NOT NULL THEN p.nombre || ' ' || p.apellido 
                ELSE p.nombre 
            END as nombre,
            p.dni as idSocio,
            'https://randomuser.me/api/portraits/women/44.jpg' as urlImagen
        FROM Persona p
        LEFT JOIN Socio s ON p.id_persona = s.id_persona
        WHERE s.id_socio IS NULL
        ORDER BY p.nombre, p.apellido
    """)
    suspend fun obtenerNoSociosComoMiembros(): List<Miembro>
}
