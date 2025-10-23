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
import kotlinx.coroutines.launch

class MiembrosActivity : AppCompatActivity() {

    private lateinit var recyclerViewMiembros: RecyclerView
    private lateinit var adapter: MiembrosAdapter
    private lateinit var btnTodos: MaterialButton
    private lateinit var btnSocios: MaterialButton
    private lateinit var btnNoSocios: MaterialButton

    private var todosMiembros: List<Miembro> = emptyList()
    private var socios: List<Miembro> = emptyList()
    private var noSocios: List<Miembro> = emptyList()

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
        adapter = MiembrosAdapter(emptyList())
        recyclerViewMiembros.adapter = adapter
        recyclerViewMiembros.layoutManager = LinearLayoutManager(this)
    }

    private fun cargarMiembrosDesdeBaseDeDatos() {
        lifecycleScope.launch {
            try {
                // Obtener instancia de la base de datos
                val database = AppDatabase.getInstance(this@MiembrosActivity)

                // Cargar TODAS las personas como miembros (esto incluye tanto socios como no-socios)
                todosMiembros = database.personaDao().obtenerTodasLasPersonasComoMiembros()

                // Cargar solo los socios
                socios = database.personaDao().obtenerSociosComoMiembros()

                // Cargar solo los no-socios (personas que no están en la tabla Socio)
                noSocios = database.personaDao().obtenerNoSociosComoMiembros()

                // Mostrar todos los miembros por defecto
                actualizarRecyclerView(todosMiembros)

                // Log para debugging
                println("DEBUG: Total miembros: ${todosMiembros.size}")
                println("DEBUG: Socios: ${socios.size}")
                println("DEBUG: No-socios: ${noSocios.size}")

            } catch (e: Exception) {
                // En caso de error, mostrar el error y usar datos de ejemplo
                println("ERROR al cargar datos de la BD: ${e.message}")
                e.printStackTrace()
                cargarDatosDeEjemplo()
            }
        }
    }

    private fun cargarDatosDeEjemplo() {
        // Datos de respaldo en caso de que no haya datos en la base de datos
        val datosEjemplo = listOf(
            Miembro("Ethan Carter", "123456", "https://randomuser.me/api/portraits/men/32.jpg"),
            Miembro("Olivia Bennett", "789012", "https://randomuser.me/api/portraits/women/44.jpg"),
            Miembro("Noah Thompson", "345678", "https://randomuser.me/api/portraits/men/3.jpg"),
            Miembro("Ava Harper", "901234", "https://randomuser.me/api/portraits/women/65.jpg"),
            Miembro("Liam Foster", "567890", "https://randomuser.me/api/portraits/men/11.jpg")
        )

        todosMiembros = datosEjemplo
        socios = datosEjemplo.take(3) // Primeros 3 como socios
        noSocios = datosEjemplo.drop(3) // Últimos 2 como no-socios

        actualizarRecyclerView(todosMiembros)
    }

    private fun actualizarRecyclerView(miembros: List<Miembro>) {
        adapter = MiembrosAdapter(miembros)
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
