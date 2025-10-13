package com.example.grupoclouds.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.grupoclouds.db.dao.* // Importa todos tus DAOs
import com.example.grupoclouds.db.entity.* // Importa todas tus entidades

@Database(
    entities = [
        Persona::class,
        Socio::class,
        Administrador::class,
        NoSocio::class,
        Actividad::class,
        Cuota::class,
        RelNoSocioActividad::class,
    ],
    version = 2 // << PASO 1: Se incrementa la versión de la base de datos
)
abstract class AppDatabase : RoomDatabase() {

    // DAOs que realizen
    abstract fun actividadDao(): ActividadDao
    abstract fun personaDao(): PersonaDao
    abstract fun adminDao(): AdminDao
    abstract fun socioDao(): SocioDao
    abstract fun noSocioDao(): NoSocioDao
    abstract fun cuotaDao(): CuotaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ClubDeportivo.db"
                )
                    // << PASO 2: Se añade la estrategia de migración para desarrollo
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
