package com.example.grupoclouds.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grupoclouds.R
import com.example.grupoclouds.db.model.SocioConDetalles

class SociosVencidosAdapter(
    private var socios: List<SocioConDetalles>
) : RecyclerView.Adapter<SociosVencidosAdapter.SocioVencidoViewHolder>() {

    class SocioVencidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreCompleto: TextView = itemView.findViewById(R.id.tv_nombre_completo)
        val dni: TextView = itemView.findViewById(R.id.tv_dni)
        val fechaVencimiento: TextView = itemView.findViewById(R.id.tv_fecha_vencimiento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocioVencidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio_vencido, parent, false)
        return SocioVencidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: SocioVencidoViewHolder, position: Int) {
        val socio = socios[position]
        val nombreCompleto = "${socio.nombre} ${socio.apellido ?: ""}".trim()

        holder.nombreCompleto.text = nombreCompleto
        holder.dni.text = socio.dni
        holder.fechaVencimiento.text = socio.cuota_hasta
    }

    override fun getItemCount(): Int {
        return socios.size
    }

    fun actualizarLista(nuevaLista: List<SocioConDetalles>) {
        socios = nuevaLista
        notifyDataSetChanged()
    }
}
