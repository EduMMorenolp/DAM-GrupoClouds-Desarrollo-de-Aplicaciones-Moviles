package com.example.grupoclouds

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.grupoclouds.db.AppDatabase
import com.example.grupoclouds.db.entity.Actividad
import com.example.grupoclouds.db.entity.Cuota
import com.example.grupoclouds.db.entity.Persona
import com.example.grupoclouds.db.entity.Socio
import com.example.grupoclouds.util.ConstantesPago
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PagosActivity : AppCompatActivity() {

    private lateinit var appDatabase: AppDatabase
    private var socioEncontrado: Socio? = null
    private var personaEncontrada: Persona? = null
    private var actividadSeleccionada: Actividad? = null
    private val listaActividades = mutableListOf<Actividad>()

    // Views
    private lateinit var radioGroupTipoUsuario: RadioGroup
    private lateinit var radioSocioSi: RadioButton
    private lateinit var radioSocioNo: RadioButton
    private lateinit var dniLabel: TextView
    private lateinit var dniInput: EditText
    private lateinit var socioInfo: TextView
    private lateinit var spinnerActividades: Spinner
    private lateinit var montoDisplay: TextView
    private lateinit var fechaInput: EditText
    private lateinit var metodoPagoInput: EditText
    private lateinit var confirmarPagoButton: Button
    private lateinit var verCuotasVencerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos)

        appDatabase = AppDatabase.getInstance(applicationContext)

        initializeViews()
        setupNavigation()
        setupListeners()
        loadActividades()
        setCurrentDate()
    }

    private fun initializeViews() {
        radioGroupTipoUsuario = findViewById(R.id.radio_group_tipo_usuario)
        radioSocioSi = findViewById(R.id.radio_socio_si)
        radioSocioNo = findViewById(R.id.radio_socio_no)
        dniLabel = findViewById(R.id.dni_label)
        dniInput = findViewById(R.id.dni_input)
        socioInfo = findViewById(R.id.socio_info)
        spinnerActividades = findViewById(R.id.spinner_actividades)
        montoDisplay = findViewById(R.id.monto_display)
        fechaInput = findViewById(R.id.fecha_input)
        metodoPagoInput = findViewById(R.id.metodo_pago_input)
        confirmarPagoButton = findViewById(R.id.confirmar_pago_button)
        verCuotasVencerButton = findViewById(R.id.ver_cuotas_vencer_button)

        // Configurar fecha como clickable para DatePicker
        fechaInput.isFocusable = false
        fechaInput.isClickable = true
    }

    private fun setupListeners() {
        // RadioGroup para cambiar entre socio y no socio
        radioGroupTipoUsuario.setOnCheckedChangeListener { _, checkedId ->
            val esSocio = checkedId == R.id.radio_socio_si
            toggleTipoUsuario(esSocio)
        }

        // Búsqueda de DNI en tiempo real
        dniInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val dni = s?.toString()?.trim()
                if (!dni.isNullOrEmpty() && dni.length >= 8) {
                    buscarSocioPorDNI(dni)
                } else {
                    limpiarInfoSocio()
                }
            }
        })

        // Selector de actividad
        spinnerActividades.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) { // 0 es el item "Seleccione una actividad"
                    actividadSeleccionada = listaActividades[position - 1]
                    montoDisplay.text = "$${actividadSeleccionada?.costoActividad ?: 0f}"
                } else {
                    actividadSeleccionada = null
                    montoDisplay.text = "Seleccione una actividad"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // DatePicker para fecha
        fechaInput.setOnClickListener {
            showDatePicker()
        }

        // Confirmar pago
        confirmarPagoButton.setOnClickListener {
            confirmarPago()
        }

        // Ver cuotas a vencer
        verCuotasVencerButton.setOnClickListener {
            startActivity(Intent(this, CuotasVencidasActivity::class.java))
        }

        // Back arrow
        findViewById<ImageView>(R.id.back_arrow).setOnClickListener {
            finish()
        }
    }

    private fun toggleTipoUsuario(esSocio: Boolean) {
        if (esSocio) {
            radioSocioSi.text = "Socio"
            dniLabel.visibility = View.VISIBLE
            dniInput.visibility = View.VISIBLE
            socioInfo.visibility = View.VISIBLE
        } else {
            radioSocioNo.text = "No Socio"
            dniLabel.visibility = View.GONE
            dniInput.visibility = View.GONE
            socioInfo.visibility = View.GONE
            limpiarInfoSocio()
        }
    }

    private fun buscarSocioPorDNI(dni: String) {
        lifecycleScope.launch {
            try {
                // Buscar persona por DNI
                val persona = appDatabase.personaDao().obtenerPersonaPorDNI(dni)
                if (persona != null) {
                    // Buscar si es socio
                    val socio = appDatabase.socioDao().obtenerSocioPorPersonaId(persona.id)

                    runOnUiThread {
                        if (socio != null) {
                            socioEncontrado = socio
                            personaEncontrada = persona
                            socioInfo.text = "Socio encontrado: ${persona.nombre} ${persona.apellido ?: ""}"
                            socioInfo.setTextColor(getColor(android.R.color.holo_green_light))
                        } else {
                            limpiarInfoSocio()
                            socioInfo.text = "La persona existe pero no es socio"
                            socioInfo.setTextColor(getColor(android.R.color.holo_orange_light))
                        }
                    }
                } else {
                    runOnUiThread {
                        limpiarInfoSocio()
                        socioInfo.text = "DNI no encontrado"
                        socioInfo.setTextColor(getColor(android.R.color.holo_red_light))
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    limpiarInfoSocio()
                    socioInfo.text = "Error al buscar DNI"
                    socioInfo.setTextColor(getColor(android.R.color.holo_red_light))
                }
            }
        }
    }

    private fun limpiarInfoSocio() {
        socioEncontrado = null
        personaEncontrada = null
        socioInfo.text = "Ingrese DNI para buscar socio"
        socioInfo.setTextColor(getColor(R.color.smalltext_color))
    }

    private fun loadActividades() {
        lifecycleScope.launch {
            try {
                appDatabase.actividadDao().getAllActividades().collect { actividades ->
                    listaActividades.clear()
                    listaActividades.addAll(actividades)

                    runOnUiThread {
                        val nombresActividades = mutableListOf("Seleccione una actividad")
                        nombresActividades.addAll(actividades.map { "${it.nombreActividad} - $${it.costoActividad}" })

                        val adapter = ArrayAdapter(
                            this@PagosActivity,
                            android.R.layout.simple_spinner_item,
                            nombresActividades
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerActividades.adapter = adapter
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@PagosActivity, "Error al cargar actividades: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setCurrentDate() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        fechaInput.setText(sdf.format(Date()))
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val fecha = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
            fechaInput.setText(fecha)
        }, year, month, day).show()
    }

    private fun confirmarPago() {
        // Validaciones
        val esSocio = radioSocioSi.isChecked

        if (esSocio && socioEncontrado == null) {
            Toast.makeText(this, "Debe buscar y seleccionar un socio válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (actividadSeleccionada == null) {
            Toast.makeText(this, "Debe seleccionar una actividad", Toast.LENGTH_SHORT).show()
            return
        }

        val fecha = fechaInput.text.toString().trim()
        val metodoPago = metodoPagoInput.text.toString().trim()

        if (fecha.isEmpty()) {
            Toast.makeText(this, "Debe ingresar una fecha de pago", Toast.LENGTH_SHORT).show()
            return
        }

        if (metodoPago.isEmpty()) {
            Toast.makeText(this, "Debe ingresar un método de pago", Toast.LENGTH_SHORT).show()
            return
        }

        // Registrar pago
        lifecycleScope.launch {
            try {
                if (esSocio) {
                    registrarPagoSocio(fecha, metodoPago)
                } else {
                    registrarPagoNoSocio(fecha, metodoPago)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@PagosActivity, "Error al registrar pago: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private suspend fun registrarPagoSocio(fecha: String, metodoPago: String) {
        val socio = socioEncontrado!!
        val actividad = actividadSeleccionada!!

        val fechaVencimiento = ConstantesPago.calcularFechaVencimiento(ConstantesPago.DIAS_PAGO_30)

        val cuota = Cuota(
            idSocio = socio.id,
            monto = actividad.costoActividad,
            fechaPago = fecha,
            fechaVence = fechaVencimiento,
            tipoPago = actividad.nombreActividad,
            metodoPago = metodoPago
        )

        appDatabase.cuotaDao().insertarCuota(cuota)

        appDatabase.socioDao().actualizarCuotaHasta(socio.id, fechaVencimiento)

        runOnUiThread {
            Toast.makeText(this@PagosActivity, "Pago registrado exitosamente para ${personaEncontrada?.nombre}", Toast.LENGTH_LONG).show()
            limpiarFormulario()
        }
    }

    private suspend fun registrarPagoNoSocio(fecha: String, metodoPago: String) {
        val actividad = actividadSeleccionada!!

        // Para no socios, registramos en la tabla de relación NoSocio-Actividad
        // (Esto depende de tu estructura de base de datos para no socios)

        runOnUiThread {
            Toast.makeText(this@PagosActivity, "Pago registrado exitosamente para no socio", Toast.LENGTH_LONG).show()
            limpiarFormulario()
        }
    }

    private fun limpiarFormulario() {
        dniInput.text.clear()
        limpiarInfoSocio()
        spinnerActividades.setSelection(0)
        actividadSeleccionada = null
        montoDisplay.text = "Seleccione una actividad"
        metodoPagoInput.text.clear()
        setCurrentDate()
        socioEncontrado = null
        personaEncontrada = null
    }

    private fun setupNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_pagos

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    true
                }
                R.id.nav_miembros -> {
                    startActivity(Intent(this, MiembrosActivity::class.java))
                    true
                }
                R.id.nav_pagos -> true
                R.id.nav_actividades -> {
                    startActivity(Intent(this, ActividadesActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}