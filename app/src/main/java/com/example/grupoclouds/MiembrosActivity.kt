/*
package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.ImageView

class MiembrosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_miembros)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_component)
        bottomNavigationView.selectedItemId = R.id.nav_miembros

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_miembros -> true
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

        val fab = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(this, EntregaCarnetSocioActivity::class.java))
        }

        val ivAddMember = findViewById<ImageView>(R.id.iv_add_member)
        ivAddMember.setOnClickListener {
            startActivity(Intent(this, AddMemberActivity::class.java))
        }
    }
}*/
package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.example.grupoclouds.db.AppDatabase
import com.example.grupoclouds.db.model.MiembroCompleto
import kotlinx.coroutines.launch

class MiembrosActivity : AppCompatActivity() {

    private lateinit var recyclerViewMiembros: RecyclerView
    private lateinit var adapter: MiembrosCompletoAdapter
    private lateinit var btnTodos: MaterialButton
    private lateinit var btnSocios: MaterialButton
    private lateinit var btnNoSocios: MaterialButton

    private var todosMiembros: List<MiembroCompleto> = emptyList()
    private var socios: List<MiembroCompleto> = emptyList()
    private var noSocios: List<MiembroCompleto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_miembros)

        // --- INICIALIZACIÓN DE VISTAS ---
        initializeViews()

        // --- CONFIGURACIÓN DEL RECYCLERVIEW ---
        setupRecyclerView()

        // --- CARGAR DATOS DESDE LA BASE DE DATOS ---
        cargarMiembrosDesdeBaseDeDatos()

        // --- CONFIGURACIÓN DE LOS LISTENERS ---
        setupNavigation()
        setupActionButtons()
        setupFilterButtons()
    }

    private fun initializeViews() {
        recyclerViewMiembros = findViewById(R.id.rv_miembros)
        btnTodos = findViewById(R.id.btn_todos)
        btnSocios = findViewById(R.id.btn_socios)
        btnNoSocios = findViewById(R.id.btn_no_socios)
    }

    private fun setupRecyclerView() {
        // Inicializar con lista vacía
        adapter = MiembrosCompletoAdapter(emptyList())
        recyclerViewMiembros.adapter = adapter
        recyclerViewMiembros.layoutManager = LinearLayoutManager(this)
    }

    private fun cargarMiembrosDesdeBaseDeDatos() {
        lifecycleScope.launch {
            try {
                // Obtener instancia de la base de datos
                val database = AppDatabase.getInstance(this@MiembrosActivity)

                // Cargar TODAS las personas con información completa
                todosMiembros = database.personaDao().obtenerTodasLasPersonasCompletas()

                // Cargar solo los socios con información completa
                socios = database.personaDao().obtenerSociosCompletos()

                // Cargar solo los no-socios
                noSocios = database.personaDao().obtenerNoSociosCompletos()

                // Mostrar todos los miembros por defecto
                actualizarRecyclerView(todosMiembros)

                // Log para debugging
                println("DEBUG: Total miembros: ${todosMiembros.size}")
                println("DEBUG: Socios: ${socios.size}")
                println("DEBUG: No-socios: ${noSocios.size}")

                // Log de datos para verificar
                todosMiembros.forEach { miembro ->
                    println("DEBUG: ${miembro.nombreCompleto()} - ${if(miembro.esSocio) "SOCIO" else "NO-SOCIO"} - Fecha nac: ${miembro.fechaNacimiento}")
                }

            } catch (e: Exception) {
                // En caso de error, mostrar el error y usar datos de ejemplo
                println("ERROR al cargar datos de la BD: ${e.message}")
                e.printStackTrace()
                cargarDatosDeEjemplo()
            }
        }
    }

    private fun cargarDatosDeEjemplo() {
        // Datos de respaldo con información completa
        val datosEjemplo = listOf(
            MiembroCompleto(
                nombre = "Eduardo",
                apellido = "Moreno",
                dni = "12345678Z",
                fechaNacimiento = "1990-01-01",
                esSocio = true,
                idSocio = "1",
                fechaAlta = "2025-01-01",
                cuotaHasta = "2025-12-31",
                tieneCarnet = true,
                tijoFichaMedica = true // Eduardo SÍ trajo su ficha médica
            ),
            MiembroCompleto(
                nombre = "Jack",
                apellido = "Herman",
                dni = "87654321A",
                fechaNacimiento = "1992-05-20",
                esSocio = false,
                idSocio = "87654321A",
                tijoFichaMedica = false // Jack NO trajo su ficha médica
            ),
            MiembroCompleto(
                nombre = "Marcelo",
                apellido = "Moreno",
                dni = "32123456",
                fechaNacimiento = "1985-03-15",
                esSocio = false,
                idSocio = "32123456",
                tijoFichaMedica = true // Marcelo SÍ trajo su ficha médica
            )
        )

        todosMiembros = datosEjemplo
        socios = datosEjemplo.filter { it.esSocio }
        noSocios = datosEjemplo.filter { !it.esSocio }

        actualizarRecyclerView(todosMiembros)
    }

    private fun actualizarRecyclerView(miembros: List<MiembroCompleto>) {
        adapter = MiembrosCompletoAdapter(miembros)
        recyclerViewMiembros.adapter = adapter
    }

    private fun setupFilterButtons() {
        // Configurar botón "Todos"
        btnTodos.setOnClickListener {
            actualizarRecyclerView(todosMiembros)
            resetButtonColors()
            btnTodos.setBackgroundColor(getColor(R.color.button_A_background))
        }

        // Configurar botón de Socios
        btnSocios.setOnClickListener {
            actualizarRecyclerView(socios)
            resetButtonColors()
            btnSocios.setBackgroundColor(getColor(R.color.button_A_background))
        }

        // Configurar botón de No-Socios
        btnNoSocios.setOnClickListener {
            actualizarRecyclerView(noSocios)
            resetButtonColors()
            btnNoSocios.setBackgroundColor(getColor(R.color.button_A_background))
        }
    }

    private fun resetButtonColors() {
        btnTodos.setBackgroundColor(getColor(R.color.btn_verde))
        btnSocios.setBackgroundColor(getColor(R.color.btn_verde))
        btnNoSocios.setBackgroundColor(getColor(R.color.btn_verde))
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
                R.id.nav_miembros -> true
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

    private fun setupActionButtons() {
        val fab = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(this, EntregaCarnetSocioActivity::class.java))
        }

        val ivAddMember = findViewById<ImageView>(R.id.iv_add_member)
        ivAddMember.setOnClickListener {
            startActivity(Intent(this, AddMemberActivity::class.java))
        }
    }
}
