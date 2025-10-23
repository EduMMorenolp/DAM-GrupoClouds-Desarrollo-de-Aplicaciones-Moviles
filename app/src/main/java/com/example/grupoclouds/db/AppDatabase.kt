package com.example.grupoclouds.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.grupoclouds.db.dao.* // Importa todos tus DAOs
import com.example.grupoclouds.db.entity.* // Importa todas tus entidades
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    version = 3 // << PASO 1: Se incrementa la versión de la base de datos para el nuevo campo email
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
                    // estrategia de migración para desarrollo y Callback para poblar la BD
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(context.applicationContext))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // LÓGICA DEL CALLBACK
    private class DatabaseCallback(private val context: Context) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Usamos una coroutine para que la inserción de datos no bloquee el hilo principal.
            CoroutineScope(Dispatchers.IO).launch {
                val database = getInstance(context)
                poblarBaseDeDatos(database)
            }
        }

        private suspend fun poblarBaseDeDatos(db: AppDatabase) {
            val personaAdmin = Persona(0, "Eduardo", "Moreno", "12345678", "eduardo.admin@email.com", "1990-01-01")
            val adminEdu = Administrador(0, "admin", "12345", "2025-10-26", 0)
            insertarAdmin(db, personaAdmin, adminEdu)

            val personaJack = Persona(1, "Marcelo", "Moreno", "87654321", "marcelo.admin@email.com", "1992-05-20")
            val adminJack = Administrador(0, "jack", "1234", "2025-10-26", 0)
            insertarAdmin(db, personaJack, adminJack)
        }
        private suspend fun insertarAdmin(db: AppDatabase, persona: Persona, admin: Administrador) {
            try {
                val idPersona = db.personaDao().insertarPersona(persona)
                val adminFinal = admin.copy(idPersona = idPersona.toInt())
                db.adminDao().insertarAdmin(adminFinal)
                println("ROOM_SEED_SUCCESS: Administrador '${admin.nombreUsuario}' insertado durante la creación de la BD.")
            } catch (e: Exception) {
                // Captura de errores por si algo fallara (ej. DNI duplicado en la misma siembra).
                println("ROOM_SEED_ERROR: Error al insertar admin '${admin.nombreUsuario}'. Causa: ${e.message}")
            }
        }
    }
}
