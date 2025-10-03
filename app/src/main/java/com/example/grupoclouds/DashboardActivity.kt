package com.example.grupoclouds

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
                    R.id.nav_dashboard -> {
                        true // Indica que el evento fue manejado
                    }

                    R.id.nav_miembros -> {
                        // TODO: Redirigir a MiembrosActivity
                        // startActivity(Intent(this, MiembrosActivity::class.java))
                        // finish() // Opcional: cierra la actividad actual
                        true
                    }

                    R.id.nav_pagos -> {
                        // TODO: Redirigir a PagosActivity
                        // startActivity(Intent(this, PagosActivity::class.java))
                        // finish()
                        true
                    }

                    R.id.nav_actividades -> {
                        // TODO: Redirigir a ActividadesActivity
                        // startActivity(Intent(this, ActividadesActivity::class.java))
                        // finish()
                        true
                    }

                    else -> false // Si el ID no coincide, no se maneja
                }
            }
    }
}