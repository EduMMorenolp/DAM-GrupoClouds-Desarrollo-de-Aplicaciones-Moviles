package com.example.grupoclouds

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grupoclouds.adapter.SociosVencidosAdapter
import com.example.grupoclouds.db.AppDatabase
import com.example.grupoclouds.viewmodel.AlertaVencimientoViewModel
import com.example.grupoclouds.viewmodel.AlertaVencimientoViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AlertaVencimientoActivity : AppCompatActivity() {

    private val viewModel: AlertaVencimientoViewModel by viewModels {
        // Inicializa la factory para poder pasar el DAO al ViewModel
        AlertaVencimientoViewModelFactory(AppDatabase.getInstance(applicationContext).socioDao())
    }

    private lateinit var sociosAdapter: SociosVencidosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerta_vencimiento)

        // Configurar la barra de herramientas
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish() // Cierra la actividad y vuelve a la anterior
        }

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_alertas_vencimiento)
        // Reutilizamos el adapter de socios vencidos, ya que muestra los mismos datos.
        sociosAdapter = SociosVencidosAdapter(emptyList())
        recyclerView.adapter = sociosAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.sociosPorVencer.collect { listaSocios ->
                // Cada vez que la lista de socios por vencer cambie en la BD, este bloque se ejecutará.
                sociosAdapter.actualizarLista(listaSocios)

                // Gestiona la visibilidad del mensaje de estado vacío.
                val emptyTextView = findViewById<TextView>(R.id.tv_empty_state_alertas)
                if (listaSocios.isEmpty()) {
                    emptyTextView.visibility = View.VISIBLE
                } else {
                    emptyTextView.visibility = View.GONE
                }
            }
        }
    }
}
