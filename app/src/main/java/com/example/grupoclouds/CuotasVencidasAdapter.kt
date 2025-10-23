package com.example.grupoclouds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.grupoclouds.db.model.SocioConDetalles
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class CuotasVencidasAdapter(private var items: List<SocioConDetalles>) : RecyclerView.Adapter<CuotasVencidasAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreSocio: TextView = view.findViewById(R.id.nombre_socio)
        val dniSocio: TextView = view.findViewById(R.id.dni_socio)
        val estadoCuota: TextView = view.findViewById(R.id.estado_cuota)
        val fechaVencimiento: TextView = view.findViewById(R.id.fecha_vencimiento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pago_vencido, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val socioInfo = items[position]

        holder.nombreSocio.text = "${socioInfo.nombre} ${socioInfo.apellido}"
        holder.dniSocio.text = "DNI: ${socioInfo.dni}"

        // --- INICIO DE LA CORRECCIÓN ---
        // Verificamos si la fecha no es nula o vacía ANTES de intentar parsearla.
        if (socioInfo.cuota_hasta.isNullOrEmpty()) {
            // Si la fecha está vacía, mostramos un estado por defecto.
            holder.fechaVencimiento.text = "Vence: -"
            holder.estadoCuota.text = "Sin pago registrado"
            holder.estadoCuota.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black)) // Un color neutro
        } else {
            // Si la fecha SÍ tiene un valor, ejecutamos la lógica que ya tenías.
            val fechaVencimientoSocio = LocalDate.parse(socioInfo.cuota_hasta)
            val hoy = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            holder.fechaVencimiento.text = "Vence: ${fechaVencimientoSocio.format(formatter)}"

            when {
                fechaVencimientoSocio.isBefore(hoy) -> {
                    val diasVencido = ChronoUnit.DAYS.between(fechaVencimientoSocio, hoy)
                    holder.estadoCuota.text = if (diasVencido == 1L) "Vencida hace 1 día" else "Vencida hace $diasVencido días"
                    holder.estadoCuota.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
                }
                fechaVencimientoSocio.isEqual(hoy) -> {
                    holder.estadoCuota.text = "Vence Hoy"
                    holder.estadoCuota.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.orange))
                }
                else -> {
                    val diasParaVencer = ChronoUnit.DAYS.between(hoy, fechaVencimientoSocio)
                    holder.estadoCuota.text = if (diasParaVencer == 1L) "Vence en 1 día" else "Vence en $diasParaVencer días"
                    holder.estadoCuota.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
                }
            }
        }
    }


    override fun getItemCount() = items.size


    fun actualizarSocios(nuevosItems: List<SocioConDetalles>) {
        this.items = nuevosItems
        notifyDataSetChanged()
    }
}


