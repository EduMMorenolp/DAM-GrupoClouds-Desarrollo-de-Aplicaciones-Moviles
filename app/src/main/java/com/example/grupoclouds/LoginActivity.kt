package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.grupoclouds.db.AppDatabase
import com.example.grupoclouds.db.entity.Persona
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<TextInputEditText>(R.id.etUsername)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvForgotPassword: TextView = findViewById(R.id.tvForgotPassword)

        // Inicializar la base de datos
        val appDatabase = AppDatabase.getInstance(applicationContext)
        val personaDao = appDatabase.personaDao() // Get DAO instance

        // Prueba rápida de inserción y consulta
        lifecycleScope.launch {
            val dniPrueba = "12345678X"
            val personaNueva = Persona(nombre = "Admin", apellido = "User", dni = dniPrueba, fechaNacimiento = "01/01/1990")
            // Corregido: El método se llama 'insertarPersona'
            personaDao.insertarPersona(personaNueva)
            Log.d("DB_TEST", "Persona de prueba insertada.")

            // Usar obtenerPersonaPorDNI para resolver la advertencia
            val personaEncontradaPorDNI = personaDao.obtenerPersonaPorDNI(dniPrueba)
            if (personaEncontradaPorDNI != null) {
                Log.d("DB_TEST", "Persona encontrada por DNI: $personaEncontradaPorDNI")

                // Usar obtenerPersonaPorId para resolver la otra advertencia
                val idPersona = personaEncontradaPorDNI.id
                val personaEncontradaPorId = personaDao.obtenerPersonaPorId(idPersona)
                Log.d("DB_TEST", "Persona encontrada por ID: $personaEncontradaPorId")
            } else {
                Log.d("DB_TEST", "No se encontró la persona con DNI: $dniPrueba")
            }
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            // Lógica de validación hardcodeada
            if (username == "admin" && password == "12345") {
                // Credenciales correctas
                Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish() // Cierra la actividad de login
            } else {
                // Credenciales incorrectas
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}