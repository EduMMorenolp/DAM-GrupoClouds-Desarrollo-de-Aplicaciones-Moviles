package com.example.grupoclouds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.grupoclouds.db.model.SocioConDetalles
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale
import kotlin.text.format

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

    private val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val socioInfo = items[position]

        holder.nombreSocio.text = "${socioInfo.nombre} ${socioInfo.apellido ?: ""}"
        holder.dniSocio.text = "DNI: ${socioInfo.dni}"

        // Si la fecha es null, significa que nunca ha pagado.
        if (socioInfo.cuota_hasta == null) {
            holder.fechaVencimiento.text = "Vence: -"
            holder.estadoCuota.text = "Sin pago registrado"
            holder.estadoCuota.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            return // Salimos temprano para no ejecutar el resto del código.
        }

        try {
            val fechaVencimiento = LocalDate.parse(socioInfo.cuota_hasta, inputFormatter)
            val hoy = LocalDate.now()

            holder.fechaVencimiento.text = "Vence: ${fechaVencimiento.format(outputFormatter)}"

            when {
                // Caso 1: La cuota ya venció (la fecha es anterior a hoy)
                fechaVencimiento.isBefore(hoy) -> {
                    val diasVencido = ChronoUnit.DAYS.between(fechaVencimiento, hoy)
                    holder.estadoCuota.text = when (diasVencido) {
                        1L -> "Vencida hace 1 día"
                        else -> "Vencida hace $diasVencido días"
                    }
                    holder.estadoCuota.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
                }
                // Caso 2: La cuota vence hoy
                fechaVencimiento.isEqual(hoy) -> {
                    holder.estadoCuota.text = "Vence Hoy"
                    holder.estadoCuota.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.orange))
                }
                // Caso 3: La cuota está por vencer en el futuro
                else -> {
                    val diasParaVencer = ChronoUnit.DAYS.between(hoy, fechaVencimiento)
                    holder.estadoCuota.text = when (diasParaVencer) {
                        1L -> "Vence en 1 día"
                        else -> "Vence en $diasParaVencer días"
                    }
                    holder.estadoCuota.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
                }
            }

        } catch (e: DateTimeParseException) {
            // Esto solo ocurriría si la fecha en la BD tiene un formato incorrecto.
            holder.fechaVencimiento.text = "Vence: -"
            holder.estadoCuota.text = "Fecha inválida"
            holder.estadoCuota.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
        }
    }



    override fun getItemCount() = items.size


    fun actualizarSocios(nuevosItems: List<SocioConDetalles>) {
        this.items = nuevosItems
        notifyDataSetChanged()
    }
}
