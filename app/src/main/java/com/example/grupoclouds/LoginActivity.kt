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
}
