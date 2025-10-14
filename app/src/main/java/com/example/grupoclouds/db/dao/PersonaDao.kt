package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grupoclouds.db.entity.Persona

@Dao
interface PersonaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPersona(persona: Persona): Long

    @Query("SELECT * FROM Persona WHERE dni = :dni")
    suspend fun obtenerPersonaPorDNI(dni: String): Persona?

    @Query("SELECT * FROM Persona WHERE id_persona = :id")
    suspend fun obtenerPersonaPorId(id: Int): Persona?
}
