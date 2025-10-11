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
import com.example.grupoclouds.viewmodel.CuotasVencidasViewModel
import com.example.grupoclouds.viewmodel.CuotasVencidasViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CuotasVencidasActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private val viewModel: CuotasVencidasViewModel by viewModels {
        // Inicializa la factory para poder pasar el DAO al ViewModel
        CuotasVencidasViewModelFactory(AppDatabase.getInstance(applicationContext).socioDao())
    }

    private lateinit var sociosVencidosAdapter: SociosVencidosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuotas_vencidas)

        // Configurar la barra de herramientas
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish() // Cierra la actividad y vuelve a la anterior
        }

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_cuotas_vencidas)
        sociosVencidosAdapter = SociosVencidosAdapter(emptyList())
        recyclerView.adapter = sociosVencidosAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.sociosVencidos.collect { listaSocios ->
                // El código dentro de collect se ejecutará cada vez que la lista de la BD cambie.
                sociosVencidosAdapter.actualizarLista(listaSocios)

                // Muestra u oculta el mensaje de estado vacío.
                val emptyTextView = findViewById<TextView>(R.id.tv_empty_state)
                if (listaSocios.isEmpty()) {
                    emptyTextView.visibility = View.VISIBLE
                } else {
                    emptyTextView.visibility = View.GONE
                }
            }
        }
    }
}
