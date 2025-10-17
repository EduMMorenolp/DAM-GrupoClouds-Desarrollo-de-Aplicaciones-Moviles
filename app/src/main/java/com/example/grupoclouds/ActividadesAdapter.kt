package com.example.grupoclouds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grupoclouds.db.entity.Actividad as ActividadEntity
import com.google.android.material.button.MaterialButton

class ActividadesAdapter(
    private var actividades: MutableList<ActividadEntity>,
    private val onEdit: (ActividadEntity) -> Unit,
    private val onDelete: (ActividadEntity) -> Unit
) : RecyclerView.Adapter<ActividadesAdapter.ActividadViewHolder>() {

    class ActividadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.text_nombreActividad)
        val instructorTextView: TextView = itemView.findViewById(R.id.text_nombreInstructor)
        val precioTextView: TextView = itemView.findViewById(R.id.text_precio)
        val tipoTextView: TextView = itemView.findViewById(R.id.text_tipoActividad)
        val horarioTextView: TextView = itemView.findViewById(R.id.text_horarioActividad)
        val capacidadTextView: TextView = itemView.findViewById(R.id.text_capacidad)
        // botones de acción en el item
        val btnEditar: MaterialButton? = itemView.findViewById(R.id.btn_editar_actividad)
        val btnEliminar: MaterialButton? = itemView.findViewById(R.id.btn_eliminar_actividad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividades, parent, false)
        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val actividad = actividades[position]

        holder.nombreTextView.text = actividad.nombreActividad
        holder.instructorTextView.text = actividad.profesorActividad
        holder.precioTextView.text = String.format("%.2f €", actividad.costoActividad)

        // Mostrar con etiquetas para coincidir con el layout
        holder.tipoTextView.text = "Tipo: ${actividad.tipoActividad}"
        holder.horarioTextView.text = "Horario: ${actividad.horarioActividad}"
        holder.capacidadTextView.text = "Capacidad: ${actividad.capacidadActividad}"

        // Click simple en el item -> editar (misma acción que el botón editar)
        holder.itemView.setOnClickListener {
            onEdit(actividad)
        }

        // Botón Editar
        holder.btnEditar?.setOnClickListener {
            onEdit(actividad)
        }

        // Botón Eliminar
        holder.btnEliminar?.setOnClickListener {
            onDelete(actividad)
        }

        // Mantengo el long click si quieres otra interacción adicional
        holder.itemView.setOnLongClickListener {
            // por compatibilidad antiguas, llamamos a eliminar en long click
            onDelete(actividad)
            true
        }
    }

    override fun getItemCount(): Int {
        return actividades.size
    }

    fun actualizarLista(nuevaLista: List<ActividadEntity>) {
        actividades.clear()
        actividades.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}