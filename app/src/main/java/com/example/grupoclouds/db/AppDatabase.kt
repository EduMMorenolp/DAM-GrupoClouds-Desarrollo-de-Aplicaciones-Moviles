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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
    abstract fun relNoSocioActividadDao(): RelNoSocioActividadDao

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


            // --- INICIO DE LA CORRECCIÓN DE INYECCIÓN DE SOCIOS ---
            // 1. Socio con CUOTA VENCIDA
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val personaSocioVencido = Persona(0, "Test", "Uno", "87654321B", "test.uno@email.com", "1985-11-20")
            val calVenc = Calendar.getInstance()
            calVenc.add(Calendar.MONTH, -1)
            val cuotaVencida = formatter.format(calVenc.time)
            val calAltaVenc = Calendar.getInstance()
            calAltaVenc.add(Calendar.MONTH, -6)
            val fechaAltaVencido = formatter.format(calAltaVenc.time)

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
            val personaSocioNuevo = Persona(0, "Test", "Dos", "12345678", "test.dos@email.com", "1990-05-15")
            val fechaAltaNuevo = formatter.format(Calendar.getInstance().time)

            // CORRECCIÓN: Se usan parámetros con nombre y se asignan los valores correctos a cada campo.
            val socioNuevo = Socio(
                id = 0,
                idPersona = 0, // Se asignará correctamente en `insertarSocioDePrueba`
                fechaAlta = fechaAltaNuevo,
                cuotaHasta = "",
                tieneCarnet = false,
                fichaMedica = true
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
