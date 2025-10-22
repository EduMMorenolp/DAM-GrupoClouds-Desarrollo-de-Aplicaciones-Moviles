package com.example.grupoclouds

import android.app.DatePickerDialog // <<< --- ¡IMPORT CORRECTO!
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar // <<< --- ¡USA java.util.Calendar!
import java.util.Date
import java.util.Locale

class AddMemberActivity : AppCompatActivity() {

    private lateinit var appDatabase: AppDatabase

    // 1. Declaración de vistas para optimización
    private lateinit var etNombre: TextInputEditText
    private lateinit var etApellido: TextInputEditText
    private lateinit var etDni: TextInputEditText
    private lateinit var etFechaNacimiento: TextInputEditText
    private lateinit var etFechaRegistro: TextInputEditText
    private lateinit var actvTipo: AutoCompleteTextView
    private lateinit var switchFichaMedica: SwitchMaterial
    private lateinit var btnClose: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_member)

        appDatabase = AppDatabase.getInstance(applicationContext)

        // 2. Inicialización de vistas una sola vez
        etNombre = findViewById(R.id.et_nombre)
        etApellido = findViewById(R.id.et_apellido)
        etDni = findViewById(R.id.et_dni)
        etFechaNacimiento = findViewById(R.id.et_fecha_nacimiento)
        etFechaRegistro = findViewById(R.id.et_fecha_registro)
        actvTipo = findViewById(R.id.actv_tipo)
        switchFichaMedica = findViewById(R.id.switch_ficha_medica)
        btnClose = findViewById(R.id.btn_close_miembro)

        setupUI()
    }

    private fun setupUI() {
        // --- LÓGICA PARA EL MENÚ DESPLEGABLE "TIPO" ---
        val memberTypes = listOf("Socio", "No Socio")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, memberTypes)
        actvTipo.setAdapter(adapter)

        // --- LÓGICA PARA LOS SELECTORES DE FECHA ---
        etFechaNacimiento.setOnClickListener { showDatePickerDialog(etFechaNacimiento) }
        etFechaRegistro.setOnClickListener { showDatePickerDialog(etFechaRegistro) }

        // --- LÓGICA PARA LOS BOTONES ---
        findViewById<Button>(R.id.btn_registrar).setOnClickListener { registrarMiembro() }
        btnClose.setOnClickListener { finish() } // Para cerrar la actividad
    }

    private fun showDatePickerDialog(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                editText.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun registrarMiembro() {
        // Usamos las propiedades de la clase, más eficiente
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val dni = etDni.text.toString().trim()
        val fechaNacimiento = etFechaNacimiento.text.toString().trim()
        val tipoMiembro = actvTipo.text.toString()
        val tieneFichaMedica = switchFichaMedica.isChecked

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || fechaNacimiento.isEmpty() || tipoMiembro.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Se lanza la coroutine en un hilo de fondo (IO)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val nuevaPersona = Persona(nombre = nombre, apellido = apellido, dni = dni, fechaNacimiento = fechaNacimiento)
                val idPersonaGenerada = appDatabase.personaDao().insertarPersona(nuevaPersona)

                val fechaRegistro = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                when (tipoMiembro) {
                    "Socio" -> {
                        val nuevoSocio = Socio(
                            idPersona = idPersonaGenerada.toInt(),
                            fechaAlta = fechaRegistro,
                            fichaMedica = tieneFichaMedica,
                            cuotaHasta = "",
                            tieneCarnet = false
                        )
                        appDatabase.socioDao().insertarSocio(nuevoSocio)
                    }
                    "No Socio" -> {
                        val nuevoNoSocio = NoSocio(idPersona = idPersonaGenerada.toInt())
                        appDatabase.noSocioDao().insertarNoSocio(nuevoNoSocio)
                    }
                }

                // Volvemos al hilo principal para actualizar la UI
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddMemberActivity, "Miembro '$nombre $apellido' registrado con éxito", Toast.LENGTH_LONG).show()
                    limpiarFormulario()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddMemberActivity, "Error al registrar: Es posible que el DNI ya exista.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun limpiarFormulario() {
        etNombre.text?.clear()
        etApellido.text?.clear()
        etDni.text?.clear()
        etFechaNacimiento.text?.clear()
        actvTipo.text?.clear()
        // Importante: También borra el texto del campo de registro
        etFechaRegistro.text?.clear()
        switchFichaMedica.isChecked = false

        etNombre.requestFocus()
    }
}
