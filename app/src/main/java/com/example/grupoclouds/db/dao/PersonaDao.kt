package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grupoclouds.db.entity.Persona

@Dao
interface PersonaDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPersona(persona: Persona): Long
    @Query("SELECT * FROM Persona WHERE dni = :dni LIMIT 1")
    suspend fun getPersonaPorDNI(dni: String): Persona?
    @Query("SELECT * FROM Persona WHERE id_persona = :id")
    suspend fun getPersonaPorID(id: Int): Persona?

}