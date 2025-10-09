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
    version = 1 // Si cambias el esquema, incrementa este número
)
abstract class AppDatabase : RoomDatabase() {

    // DAOs que realizen
    abstract fun actividadDao(): ActividadDao
    abstract fun personaDao(): PersonaDao
    abstract fun adminDao(): AdminDao

    // ej : abstract fun personaDao(): PersonaDao




    /**
     * El companion object permite crear una instancia Singleton de la base de datos,
     * asegurando que solo haya una conexión abierta a la vez en toda la app.
     */
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            // El bloque synchronized asegura que solo un hilo pueda ejecutar este código a la vez,
            // evitando que se creen dos instancias de la base de datos accidentalmente.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ClubDeportivo.db" // El nombre del archivo de la BD
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}