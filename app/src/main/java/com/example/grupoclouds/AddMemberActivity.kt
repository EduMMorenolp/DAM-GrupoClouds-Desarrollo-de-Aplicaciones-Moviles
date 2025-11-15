package com.example.grupoclouds

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.grupoclouds.db.AppDatabase
import com.example.grupoclouds.db.entity.Persona
import com.example.grupoclouds.db.entity.Socio
import com.example.grupoclouds.db.entity.NoSocio
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddMemberActivity : AppCompatActivity() {

    private lateinit var etNombre: TextInputEditText
    private lateinit var etApellido: TextInputEditText
    private lateinit var etDni: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etFechaNacimiento: TextInputEditText
    private lateinit var actvTipo: AutoCompleteTextView
    private lateinit var etFechaRegistro: TextInputEditText
    private lateinit var switchFichaMedica: SwitchMaterial
    private lateinit var btnRegistrar: MaterialButton
    private lateinit var btnClose: android.widget.ImageView

    private lateinit var database: AppDatabase
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_member)

        // Inicializar base de datos
        database = AppDatabase.getInstance(this)

        // Inicializar vistas
        initializeViews()

        // Configurar componentes
        setupDatePickers()
        setupDropdown()
        setupListeners()
    }

    private fun initializeViews() {
        etNombre = findViewById(R.id.et_nombre)
        etApellido = findViewById(R.id.et_apellido)
        etDni = findViewById(R.id.et_dni)
        etEmail = findViewById(R.id.et_email)
        etFechaNacimiento = findViewById(R.id.et_fecha_nacimiento)
        actvTipo = findViewById(R.id.actv_tipo)
        etFechaRegistro = findViewById(R.id.et_fecha_registro)
        switchFichaMedica = findViewById(R.id.switch_ficha_medica)
        btnRegistrar = findViewById(R.id.btn_registrar)
        btnClose = findViewById(R.id.btn_close_miembro)

        // Configurar fecha de registro con la fecha actual
        etFechaRegistro.setText(dateFormat.format(Date()))
    }

    private fun setupDropdown() {
        val tiposMiembro = arrayOf("Socio", "No Socio")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tiposMiembro)
        actvTipo.setAdapter(adapter)
    }

    private fun setupDatePickers() {
        // DatePicker para fecha de nacimiento
        etFechaNacimiento.setOnClickListener {
            showDatePicker { date ->
                etFechaNacimiento.setText(date)
            }
        }

        // DatePicker para fecha de registro
        etFechaRegistro.setOnClickListener {
            showDatePicker { date ->
                etFechaRegistro.setText(date)
            }
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            calendar.set(selectedYear, selectedMonth, selectedDay)
            val formattedDate = dateFormat.format(calendar.time)
            onDateSelected(formattedDate)
        }, year, month, day).show()
    }

    private fun setupListeners() {
        btnRegistrar.setOnClickListener {
            registrarMiembro()
        }

        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun registrarMiembro() {
        // Validar campos obligatorios
        val nombre = etNombre.text?.toString()?.trim()
        val dni = etDni.text?.toString()?.trim()
        val email = etEmail.text?.toString()?.trim()
        val tipo = actvTipo.text?.toString()?.trim()

        if (nombre.isNullOrEmpty()) {
            Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
            etNombre.requestFocus()
            return
        }

        if (dni.isNullOrEmpty()) {
            Toast.makeText(this, "El DNI es obligatorio", Toast.LENGTH_SHORT).show()
            etDni.requestFocus()
            return
        }

        // Validar email si se proporciona
        if (!email.isNullOrEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "El formato del email no es válido", Toast.LENGTH_SHORT).show()
            etEmail.requestFocus()
            return
        }

        if (tipo.isNullOrEmpty()) {
            Toast.makeText(this, "Debe seleccionar el tipo de miembro", Toast.LENGTH_SHORT).show()
            actvTipo.requestFocus()
            return
        }

        // Validar que la ficha médica esté activada (obligatoria)
        if (!switchFichaMedica.isChecked) {
            Toast.makeText(this, "La ficha médica es obligatoria para registrar un miembro", Toast.LENGTH_SHORT).show()
            return
        }

        // Procesar registro
        lifecycleScope.launch {
            try {
                // Verificar si el DNI ya existe
                val personaExistente = database.personaDao().obtenerPersonaPorDNI(dni)
                if (personaExistente != null) {
                    Toast.makeText(this@AddMemberActivity,
                        "Ya existe una persona con este DNI", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Crear objeto Persona
                val persona = Persona(
                    id = 0, // Se autogenera
                    nombre = nombre,
                    apellido = etApellido.text?.toString()?.trim()?.takeIf { it.isNotEmpty() },
                    dni = dni,
                    email = email.takeIf { !it.isNullOrEmpty() },
                    fechaNacimiento = etFechaNacimiento.text?.toString()?.trim()?.takeIf { it.isNotEmpty() }
                )

                // Insertar persona en la base de datos
                val personaId = database.personaDao().insertarPersona(persona)

                // Según el tipo, crear Socio o NoSocio
                when (tipo) {
                    "Socio" -> {
                        val socio = Socio(
                            id = 0, // Se autogenera
                            fechaAlta = etFechaRegistro.text?.toString()?.trim(),
                            cuotaHasta = null, // Se puede configurar después
                            tieneCarnet = false, // Inicialmente no tiene carnet
                            fichaMedica = switchFichaMedica.isChecked, // estado ficha médica
                            idPersona = personaId.toInt()
                        )
                        database.socioDao().insertarSocio(socio)
                    }
                    "No Socio" -> {
                        val noSocio = NoSocio(
                            id = 0, // Se autogenera
                            idPersona = personaId.toInt()
                        )
                        database.noSocioDao().insertarNoSocio(noSocio)
                    }
                }

                // Mostrar mensaje de éxito
                Toast.makeText(this@AddMemberActivity,
                    "Miembro registrado correctamente", Toast.LENGTH_SHORT).show()

                // Volver a la pantalla de miembros
                val intent = Intent(this@AddMemberActivity, MiembrosActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()

            } catch (e: Exception) {
                Toast.makeText(this@AddMemberActivity,
                    "Error al registrar miembro: ${e.message}", Toast.LENGTH_LONG).show()
                println("ERROR al registrar miembro: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
