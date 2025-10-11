package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.grupoclouds.db.entity.Socio

@Dao
interface SocioDao {

    /**
     * Inserta un nuevo socio en la base de datos.
     * Room ejecutará esto en una transacción.
     * @param socio El objeto Socio a insertar.
     * @return El rowId del socio recién insertado.
     */
    @Insert
    suspend fun insertarSocio(socio: Socio): Long

    /**
     * Busca un Socio en la base de datos utilizando el DNI de la Persona asociada.
     * Esta consulta requiere unir las tablas Socio y Persona.
     * @param dni El DNI de la persona a buscar.
     * @return El objeto Socio correspondiente, o null si no se encuentra.
     */
    @Query("""
        SELECT s.* FROM Socio AS s
        INNER JOIN Persona AS p ON s.id_persona_socio = p.id_persona
        WHERE p.dni = :dni
    """)
    suspend fun obtenerSocioPorDNI(dni: String): Socio?
}
