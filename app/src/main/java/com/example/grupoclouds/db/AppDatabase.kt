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
import java.time.LocalDate

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
            val personaAdmin = Persona(0, "Eduardo", "Moreno", "12345678Z", "1990-01-01")
            val adminEdu = Administrador(0, "admin", "12345", "2025-10-26", 0)
            insertarAdmin(db, personaAdmin, adminEdu)

            val personaJack = Persona(0, "Jack", "Herman", "87654321A", "1992-05-20")
            val adminJack = Administrador(0, "jack", "1234", "2025-10-26", 0)
            insertarAdmin(db, personaJack, adminJack)

            // --- INICIO DE LA CORRECCIÓN DE INYECCIÓN DE SOCIOS ---
            // 1. Socio con CUOTA VENCIDA
            val personaSocioVencido = Persona(0, "Ana", "García", "87654321B", "1985-11-20")
            val cuotaVencida = LocalDate.now().minusMonths(1).toString()
            val fechaAltaVencido = LocalDate.now().minusMonths(6).toString()

            // CORRECCIÓN: Se usan parámetros con nombre para mayor claridad y se pasan todos los argumentos requeridos.
            val socioVencido = Socio(
                id = 0,
                idPersona = 0, // Se asignará correctamente en `insertarSocioDePrueba`
                fechaAlta = fechaAltaVencido,
                cuotaHasta = cuotaVencida,
                tieneCarnet = true,
                fichaMedica = true
            )
            insertarSocioDePrueba(db, personaSocioVencido, socioVencido, "Socio Vencido")

            // 2. Socio NUEVO (sin pago registrado)
            val personaSocioNuevo = Persona(0, "Juan", "Pérez", "12345678C", "1990-05-15")
            val fechaAltaNuevo = LocalDate.now().toString()

            // CORRECCIÓN: Se usan parámetros con nombre y se asignan los valores correctos a cada campo.
            val socioNuevo = Socio(
                id = 0,
                idPersona = 0, // Se asignará correctamente en `insertarSocioDePrueba`
                fechaAlta = fechaAltaNuevo,
                cuotaHasta = "", // Cadena vacía para un socio nuevo sin pago.
                tieneCarnet = true,
                fichaMedica = false // Ajustado para tener variedad
            )
            insertarSocioDePrueba(db, personaSocioNuevo, socioNuevo, "Socio Nuevo")

            // --- FIN DE LA CORRECCIÓN ---
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

        // --- NUEVA FUNCIÓN PARA INSERTAR SOCIOS ---
        private suspend fun insertarSocioDePrueba(db: AppDatabase, persona: Persona, socio: Socio, tipo: String) {
            try {
                // 1. Inserta la persona y obtiene su ID generado automáticamente.
                val idPersona = db.personaDao().insertarPersona(persona)
                // 2. Crea una copia del socio con el ID de persona correcto.
                val socioFinal = socio.copy(idPersona = idPersona.toInt())
                // 3. Inserta el socio.
                db.socioDao().insertarSocio(socioFinal)
                println("ROOM_SEED_SUCCESS: $tipo '${persona.nombre}' insertado durante la creación de la BD.")
            } catch (e: Exception) {
                println("ROOM_SEED_ERROR: Error al insertar $tipo '${persona.nombre}'. Causa: ${e.message}")
            }
        }
    }
}
