package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.grupoclouds.db.entity.NoSocio

@Dao
interface NoSocioDao {

    /**
     * Inserta un nuevo no-socio en la base de datos.
     * @param noSocio El objeto NoSocio a insertar.
     * @return El rowId del no-socio recién insertado.
     */
    @Insert
    suspend fun insertarNoSocio(noSocio: NoSocio): Long
}
