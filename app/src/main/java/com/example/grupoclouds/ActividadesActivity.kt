package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grupoclouds.db.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class ActividadesActivity : AppCompatActivity() {

    private lateinit var appDatabase: AppDatabase
    private lateinit var adapter: ActividadesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividades)

        appDatabase = AppDatabase.getInstance(applicationContext)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewActividades)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ActividadesAdapter(mutableListOf(), onEdit = { actividad ->
            val intent = Intent(this, AddActividadActivity::class.java)
            intent.putExtra("actividad_id", actividad.id)
            startActivity(intent)
        }, onDelete = { actividad ->
            // Mostrar diálogo de confirmación antes de borrar
            AlertDialog.Builder(this)
                .setTitle(this@ActividadesActivity.getString(com.example.grupoclouds.R.string.eliminar))
                .setMessage(this@ActividadesActivity.getString(R.string.confirmar_eliminar_actividad))
                .setPositiveButton(this@ActividadesActivity.getString(R.string.eliminar)) { _, _ ->
                    // Ejecutar borrado en una corrutina
                    lifecycleScope.launch {
                        try {
                            appDatabase.actividadDao().deleteActividad(actividad)
                            runOnUiThread {
                                Toast.makeText(this@ActividadesActivity, this@ActividadesActivity.getString(R.string.actividad_eliminada), Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                Toast.makeText(this@ActividadesActivity, this@ActividadesActivity.getString(R.string.error_eliminar, e.message ?: ""), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        })

        recyclerView.adapter = adapter

        // Observa el Flow de Room para actualizar la lista en tiempo real
        lifecycleScope.launch {
            appDatabase.actividadDao().getAllActividades().collect { lista ->
                adapter.actualizarLista(lista)
            }
        }

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

    override fun onResume() {
        super.onResume()
        // Cuando volvemos de AddActividadActivity, Room Flow emitirá la lista actualizada; no es necesario hacer nada adicional
    }
}
