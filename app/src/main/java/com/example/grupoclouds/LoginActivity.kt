package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.grupoclouds.db.AppDatabase
import com.example.grupoclouds.db.entity.Administrador
import com.example.grupoclouds.db.entity.Persona
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        db = AppDatabase.getInstance(this)

        val personaAdmin = Persona(0, "Eduardo", "Moreno", "12345678", "1990-01-01")
        val adminEdu = Administrador(0, "admin", "12345", "2025-10-26", 0)
        insertarAdminSiNoExiste(personaAdmin, adminEdu)

        val personaJack = Persona(0, "Jack", "Herman", "87654321", "1992-05-20")
        val adminJack = Administrador(0, "jack", "1234", "2025-10-26", 0)
        insertarAdminSiNoExiste(personaJack, adminJack)


        val etUsername = findViewById<TextInputEditText>(R.id.etUsername)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvForgotPassword: TextView = findViewById(R.id.tvForgotPassword)


        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Por favor, introduce usuario y contraseña",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // --- Lógica de validación CONECTADA a Room ---
            lifecycleScope.launch(Dispatchers.IO) {
                // La consulta se ejecuta en un hilo de fondo (IO)
                val admin = db.adminDao().verificarCredenciales(username, password)

                // Volvemos al hilo principal para actualizar la UI (Toast, Intent, etc.)
                withContext(Dispatchers.Main) {
                    if (admin != null) {
                        // Credenciales correctas porque la consulta devolvió un usuario
                        Toast.makeText(this@LoginActivity, "Login exitoso", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Credenciales incorrectas porque la consulta no encontró a nadie
                        Toast.makeText(
                            this@LoginActivity,
                            "Usuario o contraseña incorrectos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun insertarAdminSiNoExiste(persona: Persona, admin: Administrador) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val adminExistente = db.adminDao().getAdminPorUsuario(admin.nombreUsuario)
                if (adminExistente == null) {
                    val idPersona = db.personaDao().insertPersona(persona)
                    val adminFinal = admin.copy(idPersona = idPersona.toInt())

                    db.adminDao().insertAdmin(adminFinal)
                    println("ROOM_SUCCESS: Administrador '${admin.nombreUsuario}' insertado correctamente.")
                } else {
                    // El admin ya existía, no hacemos nada
                    println("ROOM_INFO: El administrador '${admin.nombreUsuario}' ya existe.")
                }
            } catch (e: Exception) {
                println("ROOM_ERROR: Error al insertar admin '${admin.nombreUsuario}'. Causa: ${e.message}")
            }
        }
    }
}
