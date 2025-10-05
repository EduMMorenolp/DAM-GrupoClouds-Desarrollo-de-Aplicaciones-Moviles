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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MiembrosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_miembros)

        // --- CONFIGURACIÓN DEL RECYCLERVIEW ---
        setupRecyclerView()

        // --- CONFIGURACIÓN DE LOS LISTENERS ---
        setupNavigation()
        setupActionButtons()
    }

    private fun setupRecyclerView() {
        // 1. Buscamos el RecyclerView en nuestro layout
        val recyclerViewMiembros = findViewById<RecyclerView>(R.id.rv_miembros)

        // 2. Creamos la lista de datos "hardcodeados"
        val listaDeMiembros = listOf(
            Miembro("Ethan Carter", "123456", "https://randomuser.me/api/portraits/men/32.jpg"),
            Miembro("Olivia Bennett", "789012", "https://randomuser.me/api/portraits/women/44.jpg"),
            Miembro("Noah Thompson", "345678", "https://randomuser.me/api/portraits/men/3.jpg"),
            Miembro("Ava Harper", "901234", "https://randomuser.me/api/portraits/women/65.jpg"),
            Miembro("Liam Foster", "567890", "https://randomuser.me/api/portraits/men/11.jpg")
        )

        // 3. Creamos una instancia de nuestro adaptador y le pasamos la lista
        val adapter = MiembrosAdapter(listaDeMiembros)

        // 4. Conectamos el RecyclerView con el adaptador y definimos su layout
        recyclerViewMiembros.adapter = adapter
        recyclerViewMiembros.layoutManager = LinearLayoutManager(this)
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
