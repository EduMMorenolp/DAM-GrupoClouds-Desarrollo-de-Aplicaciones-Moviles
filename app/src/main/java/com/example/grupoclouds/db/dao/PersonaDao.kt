package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.grupoclouds.db.entity.Persona
import com.example.grupoclouds.db.model.MiembroCompleto

@Dao
interface PersonaDao {

    @Insert
    suspend fun insertarPersona(persona: Persona): Long

    @Query("SELECT * FROM Persona WHERE dni = :dni")
    suspend fun obtenerPersonaPorDNI(dni: String): Persona?

    // Consulta completa para obtener todas las personas con toda su información
    @Transaction
    @Query("""
        SELECT 
            p.nombre,
            p.apellido,
            p.dni,
            p.fecha_nacimiento as fechaNacimiento,
            CASE WHEN s.id_socio IS NOT NULL THEN 1 ELSE 0 END as esSocio,
            CASE 
                WHEN s.id_socio IS NOT NULL THEN CAST(s.id_socio AS TEXT)
                ELSE p.dni 
            END as idSocio,
            s.fecha_alta as fechaAlta,
            s.cuota_hasta as cuotaHasta,
            CASE WHEN s.tiene_carnet IS NOT NULL THEN s.tiene_carnet ELSE 0 END as tieneCarnet,
            0 as tijoFichaMedica
        FROM Persona p
        LEFT JOIN Socio s ON p.id_persona = s.id_persona
        ORDER BY p.nombre, p.apellido
    """)
    suspend fun obtenerTodasLasPersonasCompletas(): List<MiembroCompleto>

    // Consulta para obtener solo los socios con información completa
    @Transaction
    @Query("""
        SELECT 
            p.nombre,
            p.apellido,
            p.dni,
            p.fecha_nacimiento as fechaNacimiento,
            1 as esSocio,
            CAST(s.id_socio AS TEXT) as idSocio,
            s.fecha_alta as fechaAlta,
            s.cuota_hasta as cuotaHasta,
            s.tiene_carnet as tieneCarnet,
            0 as tijoFichaMedica
        FROM Persona p
        INNER JOIN Socio s ON p.id_persona = s.id_persona
        ORDER BY p.nombre, p.apellido
    """)
    suspend fun obtenerSociosCompletos(): List<MiembroCompleto>

    // Consulta para obtener solo los no-socios
    @Transaction
    @Query("""
        SELECT 
            p.nombre,
            p.apellido,
            p.dni,
            p.fecha_nacimiento as fechaNacimiento,
            0 as esSocio,
            p.dni as idSocio,
            null as fechaAlta,
            null as cuotaHasta,
            0 as tieneCarnet,
            0 as tijoFichaMedica
        FROM Persona p
        LEFT JOIN Socio s ON p.id_persona = s.id_persona
        WHERE s.id_socio IS NULL
        ORDER BY p.nombre, p.apellido
    """)
    suspend fun obtenerNoSociosCompletos(): List<MiembroCompleto>
}
