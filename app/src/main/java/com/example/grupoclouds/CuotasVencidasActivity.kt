package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grupoclouds.adapter.HistorialPagosAdapter
import com.example.grupoclouds.db.AppDatabase
import com.example.grupoclouds.db.model.DetalleHistorialPago
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CuotasVencidasActivity : AppCompatActivity() {

    private lateinit var appDatabase: AppDatabase
    private lateinit var adapter: HistorialPagosAdapter
    private lateinit var searchBar: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var filterVencidasHoy: CheckBox
    private lateinit var filterVencidasSemana: CheckBox
    private lateinit var filterVencidasMes: CheckBox
    private lateinit var filterTodas: CheckBox

    private var todasLasCuotas = listOf<DetalleHistorialPago>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuotas_vencidas)

        appDatabase = AppDatabase.getInstance(applicationContext)

        initializeViews()
        setupRecyclerView()
        setupListeners()
        setupNavigation()
        loadCuotasVencidas()
    }

    private fun initializeViews() {
        searchBar = findViewById(R.id.search_bar)
        recyclerView = findViewById(R.id.cuotas_vencidas_recycler)
        filterVencidasHoy = findViewById(R.id.filter_vencidas_hoy)
        filterVencidasSemana = findViewById(R.id.filter_vencidas_semana)
        filterVencidasMes = findViewById(R.id.filter_vencidas_mes)
        filterTodas = findViewById(R.id.filter_todas)
    }

    private fun setupRecyclerView() {
        adapter = HistorialPagosAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        // Back arrow
        findViewById<ImageView>(R.id.back_arrow).setOnClickListener {
            finish()
        }

        // Search functionality
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filtrarCuotas()
            }
        })

        // Filter checkboxes
        val filterListener = View.OnClickListener {
            // Asegurar que solo un filtro esté activo
            when (it.id) {
                R.id.filter_vencidas_hoy -> {
                    if (filterVencidasHoy.isChecked) {
                        filterVencidasSemana.isChecked = false
                        filterVencidasMes.isChecked = false
                        filterTodas.isChecked = false
                    }
                }
                R.id.filter_vencidas_semana -> {
                    if (filterVencidasSemana.isChecked) {
                        filterVencidasHoy.isChecked = false
                        filterVencidasMes.isChecked = false
                        filterTodas.isChecked = false
                    }
                }
                R.id.filter_vencidas_mes -> {
                    if (filterVencidasMes.isChecked) {
                        filterVencidasHoy.isChecked = false
                        filterVencidasSemana.isChecked = false
                        filterTodas.isChecked = false
                    }
                }
                R.id.filter_todas -> {
                    if (filterTodas.isChecked) {
                        filterVencidasHoy.isChecked = false
                        filterVencidasSemana.isChecked = false
                        filterVencidasMes.isChecked = false
                    }
                }
            }
            filtrarCuotas()
        }

        filterVencidasHoy.setOnClickListener(filterListener)
        filterVencidasSemana.setOnClickListener(filterListener)
        filterVencidasMes.setOnClickListener(filterListener)
        filterTodas.setOnClickListener(filterListener)
    }

    private fun loadCuotasVencidas() {
        lifecycleScope.launch {
            appDatabase.cuotaDao().obtenerHistorialPagos().collect { cuotas ->
                todasLasCuotas = cuotas
                runOnUiThread {
                    filtrarCuotas()
                }
            }
        }
    }

    private fun filtrarCuotas() {
        var cuotasFiltradas = todasLasCuotas

        // Aplicar filtro de búsqueda
        val query = searchBar.text.toString().trim()
        if (query.isNotEmpty()) {
            cuotasFiltradas = cuotasFiltradas.filter { cuota ->
                cuota.nombre.contains(query, ignoreCase = true) ||
                cuota.apellido?.contains(query, ignoreCase = true) == true ||
                cuota.tipoPago.contains(query, ignoreCase = true)
            }
        }

        // Aplicar filtros de fecha
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val hoy = Calendar.getInstance()
        val fechaHoy = sdf.format(hoy.time)

        when {
            filterVencidasHoy.isChecked -> {
                cuotasFiltradas = cuotasFiltradas.filter { cuota ->
                    try {
                        val fechaVence = sdf.parse(cuota.fechaVence)
                        val fechaHoyDate = sdf.parse(fechaHoy)
                        fechaVence != null && fechaHoyDate != null &&
                        sdf.format(fechaVence) == sdf.format(fechaHoyDate)
                    } catch (e: Exception) {
                        false
                    }
                }
            }
            filterVencidasSemana.isChecked -> {
                val semana = Calendar.getInstance()
                semana.add(Calendar.DAY_OF_MONTH, 7)
                val fechaSemana = sdf.format(semana.time)

                cuotasFiltradas = cuotasFiltradas.filter { cuota ->
                    try {
                        val fechaVence = sdf.parse(cuota.fechaVence)
                        val fechaHoyDate = sdf.parse(fechaHoy)
                        val fechaSemanaDate = sdf.parse(fechaSemana)
                        fechaVence != null && fechaHoyDate != null && fechaSemanaDate != null &&
                        fechaVence.after(fechaHoyDate) && fechaVence.before(fechaSemanaDate)
                    } catch (e: Exception) {
                        false
                    }
                }
            }
            filterVencidasMes.isChecked -> {
                val mes = Calendar.getInstance()
                mes.add(Calendar.DAY_OF_MONTH, 30)
                val fechaMes = sdf.format(mes.time)

                cuotasFiltradas = cuotasFiltradas.filter { cuota ->
                    try {
                        val fechaVence = sdf.parse(cuota.fechaVence)
                        val fechaHoyDate = sdf.parse(fechaHoy)
                        val fechaMesDate = sdf.parse(fechaMes)
                        fechaVence != null && fechaHoyDate != null && fechaMesDate != null &&
                        fechaVence.after(fechaHoyDate) && fechaVence.before(fechaMesDate)
                    } catch (e: Exception) {
                        false
                    }
                }
            }
            // Si filterTodas está marcado o ningún filtro está activo, mostrar todas
        }

        adapter.actualizarLista(cuotasFiltradas)
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
                R.id.nav_pagos -> {
                    startActivity(Intent(this, PagosActivity::class.java))
                    true
                }
                R.id.nav_actividades -> {
                    startActivity(Intent(this, ActividadesActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
