package com.example.grupoclouds

import android.app.Application
import android.util.Log
import com.example.grupoclouds.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Clase principal de la aplicación.
 * Su método onCreate() se ejecuta ANTES que cualquier Activity.
 * Es el lugar perfecto para realizar inicializaciones globales, como la de la base de datos.
 */
class ClubDeportivoApplication : Application() {

    // Usamos 'by lazy' para asegurarnos de que la base de datos se inicialice aquí
    // y solo una vez, de forma segura para los hilos (thread-safe).
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        Log.d("ClubDeportivoApp", "Application onCreate: Iniciando la aplicación...")

        // Forzamos la inicialización de la base de datos en un hilo de fondo
        // para no bloquear el arranque de la app.
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("ClubDeportivoApp", "Disparando la inicialización de la base de datos...")
            // Al acceder a 'database', se llamará a AppDatabase.getInstance() por primera vez,
            // creando la BD y ejecutando el callback para poblar los administradores.
            database.adminDao().contarAdmins() // Una operación ligera para forzar la creación.
            Log.d("ClubDeportivoApp", "La inicialización de la base de datos ha sido completada en segundo plano.")
        }
    }
}
