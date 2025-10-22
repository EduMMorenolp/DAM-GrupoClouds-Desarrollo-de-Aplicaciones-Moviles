package com.example.grupoclouds.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grupoclouds.db.entity.Administrador

@Dao
interface AdminDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarAdmin(admin: Administrador)
    @Query("""
        SELECT * FROM Administrador 
        INNER JOIN Persona ON Administrador.id_persona = Persona.id_persona
        WHERE Administrador.nombre_usuario = :username AND Administrador.contrasena = :password
    """)
    suspend fun verificarCredenciales(username: String, password: String): Administrador?

    @Query("SELECT * FROM Administrador WHERE nombre_usuario = :username")
    suspend fun getAdminPorUsuario(username: String): Administrador?

    @Query("SELECT COUNT(id_persona) FROM administrador")
    suspend fun contarAdmins(): Int
}
