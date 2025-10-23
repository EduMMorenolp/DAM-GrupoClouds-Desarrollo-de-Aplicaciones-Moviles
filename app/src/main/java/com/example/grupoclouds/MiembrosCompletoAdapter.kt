package com.example.grupoclouds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grupoclouds.db.model.MiembroCompleto

class MiembrosCompletoAdapter(private val listaMiembros: List<MiembroCompleto>) :
    RecyclerView.Adapter<MiembrosCompletoAdapter.MiembroViewHolder>() {

    /**
     * El ViewHolder describe una vista de un ítem y sus metadatos dentro del RecyclerView.
     */
    inner class MiembroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Obtenemos las referencias a las vistas dentro de item_miembro2.xml
        val imagenPerfil: ImageView = itemView.findViewById(R.id.iv_profile)
        val textoNombre: TextView = itemView.findViewById(R.id.tv_nombre_miembro)
        val textoId: TextView = itemView.findViewById(R.id.tv_id_miembro)
        val textoTipoMiembro: TextView = itemView.findViewById(R.id.tv_tipo_miembro)
        val textoFechaNacimiento: TextView = itemView.findViewById(R.id.tv_fecha_nacimiento)
        val textoEstadoInfo: TextView = itemView.findViewById(R.id.tv_estado_info)
        val textoFichaMedica: TextView = itemView.findViewById(R.id.tv_ficha_medica)
        val textoEstadoCarnet: TextView = itemView.findViewById(R.id.tv_estado_carnet)
    }

    /**
     * Se llama cuando el RecyclerView necesita un nuevo ViewHolder para representar un ítem.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiembroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_miembro2, parent, false)
        return MiembroViewHolder(view)
    }

    /**
     * Devuelve el número total de ítems en la lista de datos.
     */
    override fun getItemCount(): Int {
        return listaMiembros.size
    }

    /**
     * Se llama para mostrar los datos en una posición específica.
     */
    override fun onBindViewHolder(holder: MiembroViewHolder, position: Int) {
        val miembroActual = listaMiembros[position]

        // Asignamos los datos del miembro a las vistas
        holder.textoNombre.text = miembroActual.nombreCompleto()

        // Configuramos según si es socio o no-socio
        if (miembroActual.esSocio) {
            holder.textoId.text = "Socio N°: ${miembroActual.idSocio}"
            holder.textoTipoMiembro.text = "SOCIO"
        } else {
            holder.textoId.text = "DNI: ${miembroActual.dni}"
            holder.textoTipoMiembro.text = "NO-SOCIO"
        }

        // Mostramos fecha de nacimiento si está disponible
        val fechaNacimiento = miembroActual.getFechaNacimientoFormateada()
        if (fechaNacimiento != null) {
            holder.textoFechaNacimiento.text = fechaNacimiento
            holder.textoFechaNacimiento.visibility = View.VISIBLE
        } else {
            holder.textoFechaNacimiento.visibility = View.GONE
        }

        // Mostramos información de estado (cuota, carnet, etc.)
        val estadoInfo = miembroActual.getEstadoInfo()
        if (estadoInfo != null) {
            holder.textoEstadoInfo.text = estadoInfo
            holder.textoEstadoInfo.visibility = View.VISIBLE
        } else {
            holder.textoEstadoInfo.visibility = View.GONE
        }

        // Mostramos el estado de la ficha médica con color dinámico
        holder.textoFichaMedica.text = miembroActual.getEstadoFichaMedica()

        // Configuramos el color según si tiene o no la ficha médica
        val colorFichaMedica = if (miembroActual.tijoFichaMedica) {
            android.graphics.Color.parseColor("#4CAF50") // Verde si tiene ficha
        } else {
            android.graphics.Color.parseColor("#F44336") // Rojo si no tiene ficha
        }
        holder.textoFichaMedica.setTextColor(colorFichaMedica)

        // Mostramos el estado del carnet solo para socios
        val estadoCarnet = miembroActual.getEstadoCarnet()
        if (estadoCarnet != null && miembroActual.esSocio) {
            holder.textoEstadoCarnet.text = estadoCarnet
            holder.textoEstadoCarnet.visibility = View.VISIBLE

            // Configuramos el color según si tiene o no carnet
            val colorCarnet = if (miembroActual.tieneCarnet) {
                android.graphics.Color.parseColor("#4CAF50") // Verde si tiene carnet
            } else {
                android.graphics.Color.parseColor("#FF9800") // Naranja si no tiene carnet
            }
            holder.textoEstadoCarnet.setTextColor(colorCarnet)
        } else {
            holder.textoEstadoCarnet.visibility = View.GONE // Ocultar para no-socios
        }
    }
}
