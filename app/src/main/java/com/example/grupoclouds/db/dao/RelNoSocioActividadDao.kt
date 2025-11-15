package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.grupoclouds.db.entity.RelNoSocioActividad

@Dao
interface RelNoSocioActividadDao {

    @Insert
    suspend fun insertarRelacion(relacion: RelNoSocioActividad)

    @Query("SELECT * FROM NoSocio_Actividad WHERE id_no_socio = :idNoSocio")
    suspend fun obtenerActividadesDeNoSocio(idNoSocio: Int): List<RelNoSocioActividad>
}

