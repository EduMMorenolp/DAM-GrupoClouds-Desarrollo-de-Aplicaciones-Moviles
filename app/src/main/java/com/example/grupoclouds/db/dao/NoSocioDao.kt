package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.grupoclouds.db.entity.NoSocio
import com.example.grupoclouds.Miembro

@Dao
interface NoSocioDao {

    /**
     * Inserta un nuevo no-socio en la base de datos.
     * @param noSocio El objeto NoSocio a insertar.
     * @return El rowId del no-socio recién insertado.
     */
    @Insert
    suspend fun insertarNoSocio(noSocio: NoSocio): Long

    // Nueva consulta para obtener todos los no-socios como objetos Miembro
    @Transaction
    @Query("""
        SELECT 
            CASE 
                WHEN p.apellido IS NOT NULL THEN p.nombre || ' ' || p.apellido 
                ELSE p.nombre 
            END as nombre,
            p.dni as idSocio,
            'https://randomuser.me/api/portraits/women/44.jpg' as urlImagen
        FROM NoSocio ns
        INNER JOIN Persona p ON ns.id_persona = p.id_persona
        ORDER BY p.nombre, p.apellido
    """)
    suspend fun obtenerTodosNoSociosComoMiembros(): List<Miembro>
}
