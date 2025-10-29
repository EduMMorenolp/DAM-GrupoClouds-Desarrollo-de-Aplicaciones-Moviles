package com.example.grupoclouds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.grupoclouds.db.model.SocioConDetalles
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

        holder.nombreSocio.text = "${socioInfo.nombre} ${socioInfo.apellido ?: ""}"
        holder.dniSocio.text = "DNI: ${socioInfo.dni}"

        // --- INICIO DE LA CORRECCIÓN ---
        // Verificamos si la fecha no es nula o vacía ANTES de intentar parsearla.
        if (socioInfo.cuota_hasta.isNullOrEmpty()) {
            // Si la fecha está vacía, mostramos un estado por defecto.
            holder.fechaVencimiento.text = "Vence: -"
            holder.estadoCuota.text = "Sin pago registrado"
            holder.estadoCuota.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black)) // Un color neutro
        } else {
            try {
                val sdfInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val sdfOutput = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fechaVencimientoDate = sdfInput.parse(socioInfo.cuota_hasta)

                if (fechaVencimientoDate == null) {
                    holder.fechaVencimiento.text = "Vence: -"
                    holder.estadoCuota.text = "Sin pago registrado"
                    holder.estadoCuota.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
                } else {
                    val fechaVencCal = Calendar.getInstance().apply { time = fechaVencimientoDate }
                    val hoyCal = Calendar.getInstance()
                    // Normalizar horas a 0 para comparar solo fechas
                    listOf(hoyCal, fechaVencCal).forEach { cal ->
                        cal.set(Calendar.HOUR_OF_DAY, 0)
                        cal.set(Calendar.MINUTE, 0)
                        cal.set(Calendar.SECOND, 0)
                        cal.set(Calendar.MILLISECOND, 0)
                    }

                    holder.fechaVencimiento.text = "Vence: ${sdfOutput.format(fechaVencimientoDate)}"

                    when {
                        fechaVencCal.before(hoyCal) -> {
                            val diasVencido = ((hoyCal.timeInMillis - fechaVencCal.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
                            holder.estadoCuota.text = if (diasVencido == 1) "Vencida hace 1 día" else "Vencida hace $diasVencido días"
                            holder.estadoCuota.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
                        }
                        fechaVencCal.equals(hoyCal) -> {
                            holder.estadoCuota.text = "Vence Hoy"
                            holder.estadoCuota.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.orange))
                        }
                        else -> {
                            val diasParaVencer = ((fechaVencCal.timeInMillis - hoyCal.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
                            holder.estadoCuota.text = if (diasParaVencer == 1) "Vence en 1 día" else "Vence en $diasParaVencer días"
                            holder.estadoCuota.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
                        }
                    }
                }
            } catch (e: Exception) {
                holder.fechaVencimiento.text = "Vence: -"
                holder.estadoCuota.text = "Sin pago registrado"
                holder.estadoCuota.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            }
        }
    }


    override fun getItemCount() = items.size


    fun actualizarSocios(nuevosItems: List<SocioConDetalles>) {
        this.items = nuevosItems
        notifyDataSetChanged()
    }
}
