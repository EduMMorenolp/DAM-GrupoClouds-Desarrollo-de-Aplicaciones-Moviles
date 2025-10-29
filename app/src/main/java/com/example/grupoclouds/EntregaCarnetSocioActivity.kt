package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.grupoclouds.db.AppDatabase
import com.example.grupoclouds.db.entity.Socio
import com.example.grupoclouds.db.entity.Persona
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class EntregaCarnetSocioActivity : AppCompatActivity() {

    private lateinit var etDniInput: TextInputEditText
    private lateinit var tvNombreValue: TextView
    private lateinit var tvDniValue: TextView
    private lateinit var ivCarnetImage: ImageView
    private lateinit var btnEntregarCarnet: Button
    private lateinit var btnEnviarPorMail: Button
    private lateinit var btnClose: ImageView

    private var socioActual: Socio? = null
    private var personaActual: Persona? = null
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrega_carnet_socio)

        // Inicializar base de datos
        database = AppDatabase.getInstance(this)

        // Inicializar vistas
        initializeViews()

        // Configurar listeners
        setupListeners()
        setupNavigation()
    }

    private fun initializeViews() {
        etDniInput = findViewById(R.id.etEntregacarnetSocioDniInput)
        tvNombreValue = findViewById(R.id.tvSocioNombreValue)
        tvDniValue = findViewById(R.id.tvSocioDniValue)
        ivCarnetImage = findViewById(R.id.ivCarnetSocioImage)
        btnEntregarCarnet = findViewById(R.id.btnEntregarCarnet)
        btnEnviarPorMail = findViewById(R.id.btnEnviarPorMail)
        btnClose = findViewById(R.id.btn_close2)

        // Ocultar información inicial hasta que se ingrese un DNI válido
        ocultarInformacionSocio()
    }

    private fun setupListeners() {
        // Listener para el campo de DNI - buscar socio en tiempo real
        etDniInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val dni = s.toString().trim()
                // Buscar solo cuando el DNI tenga el formato correcto (7-8 dígitos numéricos)
                if (dni.matches(Regex("^\\d{7,8}$"))) {
                    buscarSocioPorDNI(dni)
                } else {
                    ocultarInformacionSocio()
                }
            }
        })

        // Botón entregar carnet - cambiar estado booleano
        btnEntregarCarnet.setOnClickListener {
            entregarCarnet()
        }

        // Botón enviar por mail - mostrar notificación
        btnEnviarPorMail.setOnClickListener {
            if (socioActual != null) {
                Toast.makeText(this, "Email enviado correctamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Primero seleccione un socio", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón cerrar
        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun buscarSocioPorDNI(dni: String) {
        lifecycleScope.launch {
            try {
                // Buscar persona por DNI
                personaActual = database.personaDao().obtenerPersonaPorDNI(dni)

                if (personaActual != null) {
                    // Verificar si la persona es socio
                    socioActual = database.socioDao().obtenerSocioPorPersonaId(personaActual!!.id)

                    if (socioActual != null) {
                        // Es un socio válido, mostrar información
                        mostrarInformacionSocio()
                    } else {
                        // La persona existe pero no es socio
                        Toast.makeText(this@EntregaCarnetSocioActivity,
                            "Esta persona no es socio del club", Toast.LENGTH_SHORT).show()
                        ocultarInformacionSocio()
                    }
                } else {
                    // No se encontró la persona
                    ocultarInformacionSocio()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EntregaCarnetSocioActivity,
                    "Error al buscar socio: ${e.message}", Toast.LENGTH_SHORT).show()
                ocultarInformacionSocio()
            }
        }
    }

    private fun mostrarInformacionSocio() {
        personaActual?.let { persona ->
            socioActual?.let { socio ->
                // Mostrar información del socio
                val nombreCompleto = if (persona.apellido != null) {
                    "${persona.nombre} ${persona.apellido}"
                } else {
                    persona.nombre
                }

                tvNombreValue.text = nombreCompleto
                tvDniValue.text = persona.dni

                // Mostrar imagen del carnet (puedes usar una imagen por defecto)
                ivCarnetImage.setImageResource(R.drawable.ic_person) // Usar el icono de persona como placeholder

                // Actualizar texto del botón según el estado del carnet
                actualizarBotonEntregarCarnet(socio.tieneCarnet)

                // Mostrar vistas
                tvNombreValue.visibility = android.view.View.VISIBLE
                tvDniValue.visibility = android.view.View.VISIBLE
                ivCarnetImage.visibility = android.view.View.VISIBLE
                btnEntregarCarnet.visibility = android.view.View.VISIBLE
                btnEnviarPorMail.visibility = android.view.View.VISIBLE
            }
        }
    }

    private fun ocultarInformacionSocio() {
        tvNombreValue.text = ""
        tvDniValue.text = ""
        tvNombreValue.visibility = android.view.View.GONE
        tvDniValue.visibility = android.view.View.GONE
        ivCarnetImage.visibility = android.view.View.GONE
        btnEntregarCarnet.visibility = android.view.View.GONE
        btnEnviarPorMail.visibility = android.view.View.GONE
        socioActual = null
        personaActual = null
    }

    private fun actualizarBotonEntregarCarnet(tieneCarnet: Boolean) {
        if (tieneCarnet) {
            btnEntregarCarnet.text = "Carnet ya entregado"
            btnEntregarCarnet.isEnabled = false
            btnEntregarCarnet.alpha = 0.6f
        } else {
            btnEntregarCarnet.text = "Entregar Carnet"
            btnEntregarCarnet.isEnabled = true
            btnEntregarCarnet.alpha = 1.0f
        }
    }

    private fun entregarCarnet() {
        socioActual?.let { socio ->
            if (!socio.tieneCarnet) {
                lifecycleScope.launch {
                    try {
                        // Actualizar el estado del carnet en la base de datos
                        database.socioDao().actualizarEstadoCarnet(socio.id, true)

                        // Actualizar el objeto local
                        socioActual = socio.copy(tieneCarnet = true)

                        // Actualizar UI
                        actualizarBotonEntregarCarnet(true)

                        Toast.makeText(this@EntregaCarnetSocioActivity,
                            "Carnet entregado correctamente", Toast.LENGTH_SHORT).show()

                    } catch (e: Exception) {
                        Toast.makeText(this@EntregaCarnetSocioActivity,
                            "Error al entregar carnet: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_component)
        bottomNavigationView.selectedItemId = R.id.nav_miembros

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_miembros -> {
                    startActivity(Intent(this, MiembrosActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_pagos -> {
                    startActivity(Intent(this, PagosActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_actividades -> {
                    startActivity(Intent(this, ActividadesActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
