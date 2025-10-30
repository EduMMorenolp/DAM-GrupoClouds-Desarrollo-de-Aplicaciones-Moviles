package com.example.grupoclouds

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.grupoclouds.db.AppDatabase
import com.google.android.material.appbar.MaterialToolbar
import com.example.grupoclouds.db.entity.Actividad as ActividadEntity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class AddActividadActivity : AppCompatActivity() {

    private var editingId: Int? = null
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_actividad)

        appDatabase = AppDatabase.getInstance(applicationContext)

        // Insets handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.tv_nuevaActividadTitle)
        val btnClose = findViewById<ImageView>(R.id.btn_close_actividad)
        btnClose.setOnClickListener {
            finish()
        }

        val etNombre = findViewById<TextInputEditText>(R.id.et_nombreActividad)
        val etTipo = findViewById<TextInputEditText>(R.id.et_tipoDeActividad)
        val etInstructor = findViewById<TextInputEditText>(R.id.et_instructor)
        val etHorario = findViewById<TextInputEditText>(R.id.et_horario)
        val etCapacidad = findViewById<TextInputEditText>(R.id.et_capacidad)
        val etCosto = findViewById<TextInputEditText>(R.id.et_costo)
        val btnGuardar = findViewById<MaterialButton>(R.id.btn_guardarActividad)

        // Comprobar si venimos en modo edición
        val idFromIntent = intent.getIntExtra("actividad_id", 0)
        if (idFromIntent != 0) {
            editingId = idFromIntent
            // Cambiar título a modo edición
            toolbar.title = getString(R.string.editar_actividad)

            lifecycleScope.launch {
                val actividad = appDatabase.actividadDao().getActividadById(idFromIntent)
                actividad?.let {
                    etNombre.setText(it.nombreActividad)
                    etTipo.setText(it.tipoActividad)
                    etInstructor.setText(it.profesorActividad)
                    etHorario.setText(it.horarioActividad)
                    etCapacidad.setText(it.capacidadActividad.toString())
                    etCosto.setText(it.costoActividad.toString())
                }
            }
        } else {
            // Modo crear: aseguramos título por defecto
            toolbar.title = getString(R.string.nuevaActividad)
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text?.toString()?.trim().orEmpty()
            val tipo = etTipo.text?.toString()?.trim().orEmpty()
            val instructor = etInstructor.text?.toString()?.trim().orEmpty()
            val horario = etHorario.text?.toString()?.trim().orEmpty()
            val capacidad = etCapacidad.text?.toString()?.toIntOrNull() ?: 0
            val costo = etCosto.text?.toString()?.toFloatOrNull() ?: 0f

            if (nombre.isEmpty() || tipo.isEmpty() || instructor.isEmpty()) {
                runOnUiThread { android.widget.Toast.makeText(this, "Por favor completa los campos obligatorios", android.widget.Toast.LENGTH_SHORT).show() }
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val actividadEntity = ActividadEntity(
                    id = editingId ?: 0,
                    nombreActividad = nombre,
                    tipoActividad = tipo,
                    profesorActividad = instructor,
                    horarioActividad = horario,
                    capacidadActividad = capacidad,
                    costoActividad = costo
                )

                if (editingId != null) {
                    appDatabase.actividadDao().updateActividad(actividadEntity)
                } else {
                    appDatabase.actividadDao().insertActividad(actividadEntity)
                }

                // Volver a la lista
                runOnUiThread { finish() }
            }
        }
    }
}
