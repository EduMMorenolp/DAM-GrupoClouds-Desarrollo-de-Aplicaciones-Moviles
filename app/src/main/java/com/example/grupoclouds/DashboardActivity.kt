package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_dashboard)
            // --- CÓDIGO PARA LA NAVEGACIÓN ---
            // 1. Obtenemos la referencia a la BottomNavigationView
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

            // 2. Marcamos el ítem del dashboard como seleccionado por defecto
            bottomNavigationView.selectedItemId = R.id.nav_dashboard

            // 3. Creamos el listener para manejar los clics
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    // Si el usuario ya está en Dashboard, no hacemos nada.
                    R.id.nav_dashboard -> {true}

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