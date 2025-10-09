package com.example.grupoclouds.db.dao

import androidx.room.*
import com.example.grupoclouds.db.entity.Actividad
import kotlinx.coroutines.flow.Flow

@Dao // 1. Marcamos la interfaz como un DAO
interface ActividadDao {

    // --- Operación de INSERCIÓN ---
    @Insert(onConflict = OnConflictStrategy.REPLACE) // 2. Si insertas una actividad con un ID que ya existe, la reemplaza.
    suspend fun insertActividad(actividad: Actividad) // 3. "suspend" es clave para la asincronía.

    // --- Operación de ACTUALIZACIÓN ---
    @Update
    suspend fun updateActividad(actividad: Actividad)

    // --- Operación de BORRADO ---
    @Delete
    suspend fun deleteActividad(actividad: Actividad)

    // --- Operaciones de CONSULTA (Query) ---
    @Query("SELECT * FROM Actividad ORDER BY nombre_actividad ASC") // 4. Una consulta SQL para obtener todas las actividades.
    fun getAllActividades(): Flow<List<Actividad>> // 5. "Flow" para que la UI se actualice sola.

    @Query("SELECT * FROM Actividad WHERE id_actividad = :id") // 6. Consulta con un parámetro.
    suspend fun getActividadById(id: Int): Actividad? // Devuelve una sola actividad o null si no la encuentra.
}