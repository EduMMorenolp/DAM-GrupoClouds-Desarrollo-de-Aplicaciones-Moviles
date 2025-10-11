package com.example.grupoclouds

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.grupoclouds.db.AppDatabase
import com.example.grupoclouds.db.entity.NoSocio
import com.example.grupoclouds.db.entity.Persona
import com.example.grupoclouds.db.entity.Socio
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddMemberActivity : AppCompatActivity() {

    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_member)

        // Inicializar la base de datos
        appDatabase = AppDatabase.getInstance(applicationContext)

        // Configurar el botón de cerrar
        findViewById<ImageView>(R.id.btn_close_miembro).setOnClickListener {
            finish() // Cierra la actividad actual
        }

        // Configurar el menú desplegable para tipo de miembro
        val memberTypes = arrayOf("Socio", "No Socio")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, memberTypes)
        findViewById<AutoCompleteTextView>(R.id.actv_tipo).setAdapter(adapter)

        // Configurar el botón de registro
        findViewById<Button>(R.id.btn_registrar).setOnClickListener {
            registrarMiembro()
        }
    }

    private fun registrarMiembro() {
        val nombre = findViewById<TextInputEditText>(R.id.et_nombre).text.toString().trim()
        val apellido = findViewById<TextInputEditText>(R.id.et_apellido).text.toString().trim()
        val dni = findViewById<TextInputEditText>(R.id.et_dni).text.toString().trim()
        val fechaNacimiento = findViewById<TextInputEditText>(R.id.et_fecha_nacimiento).text.toString().trim()
        val tipoMiembro = findViewById<AutoCompleteTextView>(R.id.actv_tipo).text.toString()
        val tieneFichaMedica = findViewById<SwitchMaterial>(R.id.switch_ficha_medica).isChecked

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || fechaNacimiento.isEmpty() || tipoMiembro.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                // Paso 1 y 2: Insertar Persona y obtener el ID generado
                val nuevaPersona = Persona(nombre = nombre, apellido = apellido, dni = dni, fechaNacimiento = fechaNacimiento)
                val idPersonaGenerada = appDatabase.personaDao().insertarPersona(nuevaPersona)

                // Paso 3: Usar idPersonaGenerada para crear Socio o NoSocio
                val fechaRegistro = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

                when (tipoMiembro) {
                    "Socio" -> {
                        val nuevoSocio = Socio(
                            id_persona_socio = idPersonaGenerada.toInt(),
                            fecha_registro = fechaRegistro,
                            estado_ficha_medica = if (tieneFichaMedica) 1 else 0
                        )
                        appDatabase.socioDao().insertarSocio(nuevoSocio)
                    }
                    "No Socio" -> {
                        val nuevoNoSocio = NoSocio(
                            id_persona_no_socio = idPersonaGenerada.toInt(),
                            fecha_registro = fechaRegistro
                        )
                        appDatabase.noSocioDao().insertarNoSocio(nuevoNoSocio)
                    }
                }

                runOnUiThread {
                    Toast.makeText(this@AddMemberActivity, "Miembro '$nombre $apellido' registrado con éxito", Toast.LENGTH_LONG).show()
                    limpiarFormulario() // Limpia el formulario para un nuevo registro
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@AddMemberActivity, "Error al registrar: Es posible que el DNI ya exista.", Toast.LENGTH_LONG).show()
                    // Opcional: limpiar solo campos incorrectos o todos, según prefieras
                    // limpiarFormulario()
                }
            }
        }
    }

    private fun limpiarFormulario() {
        findViewById<TextInputEditText>(R.id.et_nombre).text?.clear()
        findViewById<TextInputEditText>(R.id.et_apellido).text?.clear()
        findViewById<TextInputEditText>(R.id.et_dni).text?.clear()
        findViewById<TextInputEditText>(R.id.et_fecha_nacimiento).text?.clear()
        findViewById<AutoCompleteTextView>(R.id.actv_tipo).text?.clear()
        findViewById<SwitchMaterial>(R.id.switch_ficha_medica).isChecked = false

        // Devuelve el foco al primer campo para una entrada rápida
        findViewById<TextInputEditText>(R.id.et_nombre).requestFocus()
    }
}