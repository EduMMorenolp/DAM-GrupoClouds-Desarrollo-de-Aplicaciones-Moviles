package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.grupoclouds.db.entity.Socio

@Dao
interface SocioDao {

    /**
     * Inserta un nuevo socio en la base de datos.
     * @return El rowId del socio recién insertado.
     */
    @Insert
    suspend fun insertarSocio(socio: Socio): Long

    /**
     * Busca un Socio utilizando el DNI de la Persona asociada.
     * @return El objeto Socio correspondiente, o null si no se encuentra.
     */
    @Query("""
        SELECT s.* FROM Socio AS s
        INNER JOIN Persona AS p ON s.id_persona_socio = p.id_persona
        WHERE p.dni = :dni
    """)
    suspend fun obtenerSocioPorDNI(dni: String): Socio?

    /**
     * Actualiza la fecha de vencimiento de la cuota para un socio específico.
     *
     * @param idSocio El ID del socio que se va a actualizar.
     * @param nuevaFechaVencimiento El nuevo valor para la columna 'cuota_hasta'.
     */
    @Query("UPDATE Socio SET cuota_hasta = :nuevaFechaVencimiento WHERE id_socio = :idSocio")
    suspend fun actualizarFechaVencimiento(idSocio: Int, nuevaFechaVencimiento: String)
}
