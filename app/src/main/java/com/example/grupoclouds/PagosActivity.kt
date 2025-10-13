package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grupoclouds.adapter.HistorialPagosAdapter
import com.example.grupoclouds.db.AppDatabase
import com.example.grupoclouds.db.entity.Cuota
import com.example.grupoclouds.db.entity.Socio
import com.example.grupoclouds.util.ConstantesPago
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PagosActivity : AppCompatActivity() {

    private lateinit var appDatabase: AppDatabase
    private var socioEncontrado: Socio? = null
    private lateinit var historialPagosAdapter: HistorialPagosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos)

        appDatabase = AppDatabase.getInstance(applicationContext)
        setupNavigation()
        setupRecyclerView()
        setupListeners()
        observeHistorialPagos()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.pagos_vencidos_recycler)
        historialPagosAdapter = HistorialPagosAdapter(emptyList()) // Inicializa con una lista vacía
        recyclerView.adapter = historialPagosAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeHistorialPagos() {
        lifecycleScope.launch {
            appDatabase.cuotaDao().obtenerHistorialPagos().collect { listaDePagos ->
                runOnUiThread {
                    historialPagosAdapter.actualizarLista(listaDePagos)
                }
            }
        }
    }

    private fun setupListeners() {
        val searchBar = findViewById<EditText>(R.id.search_bar)
        val btnRegistrarPago = findViewById<Button>(R.id.confirmar_pago_button)

        searchBar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val dni = v.text.toString().trim()
                if (dni.isNotEmpty()) {
                    buscarSocioPorDNI(dni)
                }
                true
            } else {
                false
            }
        }

        btnRegistrarPago.setOnClickListener {
            val socio = socioEncontrado
            if (socio == null) {
                Toast.makeText(this, "Por favor, busque y encuentre un socio válido primero", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val monto = findViewById<EditText>(R.id.monto_input).text.toString().toFloatOrNull()
            val fechaPago = findViewById<EditText>(R.id.fecha_input).text.toString()
            val metodoPago = findViewById<EditText>(R.id.metodo_pago_input).text.toString()

            if (monto == null || fechaPago.isBlank() || metodoPago.isBlank()) {
                Toast.makeText(this, "Por favor, complete todos los campos del pago", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registrarTransaccionPago(socio, monto, fechaPago, metodoPago)
        }
    }

    private fun registrarTransaccionPago(socio: Socio, monto: Float, fechaPago: String, metodoPago: String) {
        lifecycleScope.launch {
            try {
                val diasParaSumar: Int
                val tipoPago: String

                when (monto) {
                    ConstantesPago.VALOR_MONTO_1_MES -> {
                        diasParaSumar = ConstantesPago.DIAS_PAGO_30
                        tipoPago = "Plan Mensual"
                    }
                    ConstantesPago.VALOR_MONTO_15_DIAS -> {
                        diasParaSumar = ConstantesPago.DIAS_PAGO_15
                        tipoPago = "Plan 15 Días"
                    }
                    else -> {
                        runOnUiThread { Toast.makeText(this@PagosActivity, "El monto no corresponde a un plan válido", Toast.LENGTH_LONG).show() }
                        return@launch
                    }
                }
                val fechaVencimientoCalculada = ConstantesPago.calcularFechaVencimiento(diasParaSumar)

                val nuevaCuota = Cuota(
                    idSocio = socio.id,
                    monto = monto,
                    fechaPago = fechaPago,
                    fechaVence = fechaVencimientoCalculada,
                    tipoPago = tipoPago,
                    metodoPago = metodoPago
                )

                appDatabase.cuotaDao().insertarCuota(nuevaCuota)
                appDatabase.socioDao().actualizarFechaVencimiento(socio.id, fechaVencimientoCalculada)

                runOnUiThread {
                    Toast.makeText(this@PagosActivity, "Pago registrado con éxito. Nueva fecha de vencimiento: $fechaVencimientoCalculada", Toast.LENGTH_LONG).show()
                    limpiarFormulario()
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@PagosActivity, "Error al registrar el pago: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun limpiarFormulario() {
        findViewById<EditText>(R.id.search_bar).text.clear()
        findViewById<EditText>(R.id.fecha_input).text.clear()
        findViewById<EditText>(R.id.monto_input).text.clear()
        findViewById<EditText>(R.id.metodo_pago_input).text.clear()
        socioEncontrado = null
        findViewById<EditText>(R.id.search_bar).requestFocus()
    }

    private fun buscarSocioPorDNI(dni: String) {
        lifecycleScope.launch {
            val socio = appDatabase.socioDao().obtenerSocioPorDNI(dni)
            socioEncontrado = socio

            runOnUiThread {
                if (socio != null) {
                    Toast.makeText(this@PagosActivity, "Socio encontrado. ID: ${socio.id}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@PagosActivity, "Socio no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
        }
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