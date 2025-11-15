package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class CuotasVencidasActivity : AppCompatActivity() {


    private val viewModel: CuotasVencidasViewModel by viewModels()


    private lateinit var adapter: CuotasVencidasAdapter

    private lateinit var searchBar: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var filterVencidas: CheckBox
    private lateinit var filterVencenHoy: CheckBox
    private lateinit var filterVencenSemana: CheckBox
    private lateinit var filterTodas: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuotas_vencidas)


        initializeViews()
        setupRecyclerView()
        setupListeners()
        setupNavigation()
        setupObservers()
    }
    private fun initializeViews() {
        searchBar = findViewById(R.id.search_bar)
        recyclerView = findViewById(R.id.cuotas_vencidas_recycler)
        filterVencidas = findViewById(R.id.filter_vencidas)
        filterVencenHoy = findViewById(R.id.filter_vencen_hoy)
        filterVencenSemana = findViewById(R.id.filter_vencen_semana)
        filterTodas = findViewById(R.id.filter_todas)
    }

    private fun setupRecyclerView() {
        adapter = CuotasVencidasAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        findViewById<ImageView>(R.id.back_arrow).setOnClickListener {
            finish()
        }

        // Búsqueda dinámica. Llama al ViewModel en cada cambio de texto.
        searchBar.addTextChangedListener { text ->
            viewModel.aplicarBusqueda(text.toString())
        }


        val checkBoxes = listOf(filterTodas, filterVencidas, filterVencenHoy, filterVencenSemana)

        checkBoxes.forEach { checkBox ->
            checkBox.setOnClickListener {
                if (checkBox.isChecked) {
                    checkBoxes.filter { it != checkBox }.forEach { it.isChecked = false }
                    aplicarFiltroSeleccionado()
                } else {
                    if (checkBoxes.none { it.isChecked }) {
                        filterTodas.isChecked = true
                        aplicarFiltroSeleccionado()
                    }
                }
            }
        }

        filterTodas.isChecked = true
    }

    private fun aplicarFiltroSeleccionado() {
        when {
            filterTodas.isChecked -> viewModel.aplicarFiltro(FiltroCuota.TODAS)
            filterVencidas.isChecked -> viewModel.aplicarFiltro(FiltroCuota.VENCIDAS)
            filterVencenHoy.isChecked -> viewModel.aplicarFiltro(FiltroCuota.VENCEN_HOY)
            filterVencenSemana.isChecked -> viewModel.aplicarFiltro(FiltroCuota.VENCEN_SEMANA)
        }
    }

    // 3. La Activity ya no carga datos. Solo observa los cambios del ViewModel.
    private fun setupObservers() {
        viewModel.socios.observe(this) { socios ->
            adapter.actualizarSocios(socios)
        }

        viewModel.cargando.observe(this) { estaCargando ->
            // Aquí podrías mostrar/ocultar un ProgressBar.
            // Por ejemplo: findViewById<ProgressBar>(R.id.progressBar).visibility = if (estaCargando) View.VISIBLE else View.GONE
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
                R.id.nav_pagos -> {
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
