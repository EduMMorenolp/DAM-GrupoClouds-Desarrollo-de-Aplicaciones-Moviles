package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ActividadesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividades)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewActividades)
        // Corregido: Se añaden instructores y se asegura que los parámetros coincidan con el constructor de la Entidad Actividad.
        val listaDeActividades = listOf(
            Actividad(nombre = "Yoga Matutino", instructor = "Ana Pérez", precio = "$25"),
            Actividad(nombre = "Spinning de Alta Intensidad", instructor = "Carlos Ruiz", precio = "$30"),
            Actividad(nombre = "Pilates para principiantes", instructor = "Sofía Gómez", precio = "$20"),
            Actividad(nombre = "Boxeo Fitness", instructor = "David Moreno", precio = "$25"),
            Actividad(nombre = "Zumba Party", instructor = "Laura Fernández", precio = "$15")
        )
        val adapter = ActividadesAdapter(listaDeActividades)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val ivAddActividad = findViewById<ImageView>(R.id.iv_add_actividades)
        ivAddActividad.setOnClickListener {
            startActivity(Intent(this, AddActividadActivity::class.java))
        }
        
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
