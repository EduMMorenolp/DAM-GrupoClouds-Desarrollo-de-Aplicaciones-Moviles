package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Configurar la navegación inferior
        setupBottomNavigation()

        // Configurar los botones de prueba para las nuevas pantallas
        setupTestButtons()
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_dashboard

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> true
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

    private fun setupTestButtons() {
        val btnCuotasVencidas = findViewById<Button>(R.id.btn_ver_cuotas_vencidas)
        val btnAlertasVencimiento = findViewById<Button>(R.id.btn_ver_alertas_vencimiento)

        btnCuotasVencidas.setOnClickListener {
            val intent = Intent(this, CuotasVencidasActivity::class.java)
            startActivity(intent)
        }

        btnAlertasVencimiento.setOnClickListener {
            val intent = Intent(this, AlertaVencimientoActivity::class.java)
            startActivity(intent)
        }
    }
}