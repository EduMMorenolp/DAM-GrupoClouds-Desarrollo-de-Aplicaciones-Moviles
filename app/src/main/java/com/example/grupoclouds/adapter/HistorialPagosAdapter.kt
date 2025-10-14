package com.example.grupoclouds.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grupoclouds.R
import com.example.grupoclouds.db.model.DetalleHistorialPago

class HistorialPagosAdapter(
    private var historial: List<DetalleHistorialPago>
) : RecyclerView.Adapter<HistorialPagosAdapter.PagoViewHolder>() {

    /**
     * ViewHolder que contiene las referencias a las vistas de cada fila (item).
     */
    class PagoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreSocio: TextView = itemView.findViewById(R.id.nombre_socio)
        val tipoPago: TextView = itemView.findViewById(R.id.tipo_pago)
        val montoPago: TextView = itemView.findViewById(R.id.monto_pago)
    }

    /**
     * Se llama cuando RecyclerView necesita un nuevo ViewHolder. Infla el layout de la fila.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pago_vencido, parent, false)
        return PagoViewHolder(view)
    }

    /**
     * Se llama para mostrar los datos en una posición específica. Vincula los datos con las vistas.
     */
    override fun onBindViewHolder(holder: PagoViewHolder, position: Int) {
        val pago = historial[position]

        // Combina nombre y apellido
        val nombreCompleto = "${pago.nombre} ${pago.apellido ?: ""}".trim()
        holder.nombreSocio.text = nombreCompleto

        // Asigna el tipo de pago y el monto
        holder.tipoPago.text = pago.tipoPago
        holder.montoPago.text = String.format("$%.2f", pago.monto) // Formatea el monto como moneda
    }

    /**
     * Devuelve el número total de items en la lista.
     */
    override fun getItemCount(): Int {
        return historial.size
    }

    /**
     * Función para actualizar la lista de pagos en el adapter y notificar al RecyclerView.
     */
    fun actualizarLista(nuevaLista: List<DetalleHistorialPago>) {
        historial = nuevaLista
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }
}
