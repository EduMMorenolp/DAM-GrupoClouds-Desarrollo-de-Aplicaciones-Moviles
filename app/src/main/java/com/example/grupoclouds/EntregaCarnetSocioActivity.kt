package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView

class EntregaCarnetSocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrega_carnet_socio)

        val btnClose = findViewById<ImageView>(R.id.btn_close2)
        btnClose.setOnClickListener {
            startActivity(Intent(this, MiembrosActivity::class.java))
            finish()
        }

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