package com.example.grupoclouds

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ActividadesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actividades)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //1. Encuentra el RecyclerView en tu layout por su ID
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewActividades)

        // 2. Prepara tus datos. Por ahora, creamos una lista de ejemplo.
        // Más adelante, estos datos vendrán de una base de datos, una API, etc.
        val listaDeActividades = listOf(
            Actividad("Yoga Matutino", "Lunes - 8:00 AM", precio = "$25"),
            Actividad("Spinning de Alta Intensidad", "Martes - 6:00 PM", precio = "$30"),
            Actividad("Pilates para principiantes", "Miércoles - 9:00 AM", precio = "$20"),
            Actividad("Boxeo Fitness", "Jueves - 7:00 PM", precio = "$25"),
            Actividad("Zumba Party", "Viernes - 5:00 PM", precio = "$15")
        )

        // 3. Crea una instancia de tu Adapter.
        // El Adapter es el puente entre tu lista de datos (listaDeActividades) y el RecyclerView.
        // **NOTA:** Necesitas crear el archivo `ActividadesAdapter.kt` y la clase `Actividad.kt`.
        val adapter = ActividadesAdapter(listaDeActividades)

        // 4. Configura el RecyclerView.
        // a) Define cómo se ordenarán los elementos (en este caso, una lista vertical)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // b) Asigna el adapter que acabas de crear.
        recyclerView.adapter = adapter
    }
}