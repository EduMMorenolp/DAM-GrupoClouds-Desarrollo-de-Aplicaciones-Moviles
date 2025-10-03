package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ActividadesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividades)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewActividades)
        val listaDeActividades = listOf(
            Actividad("Yoga Matutino", "Lunes - 8:00 AM", precio = "$25"),
            Actividad("Spinning de Alta Intensidad", "Martes - 6:00 PM", precio = "$30"),
            Actividad("Pilates para principiantes", "Miércoles - 9:00 AM", precio = "$20"),
            Actividad("Boxeo Fitness", "Jueves - 7:00 PM", precio = "$25"),
            Actividad("Zumba Party", "Viernes - 5:00 PM", precio = "$15")
        )
        val adapter = ActividadesAdapter(listaDeActividades)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Corrección: usar el ID correcto
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_component)
        bottomNavigationView.selectedItemId = R.id.nav_actividades

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
                R.id.nav_actividades -> true
                else -> false
            }
        }
    }
}