/*
package com.example.grupoclouds  // Tu paquete

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.animation.with
import androidx.compose.ui.semantics.text
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // Importante: librería para cargar imágenes
import com.example.dam_grupoclouds_desarrollo_de_aplicaciones_moviles.databinding.ItemMiembroBinding // Importa el ViewBinding del ítem

class MiembrosAdapter(private val miembros: List<Miembro>) : RecyclerView.Adapter<MiembrosAdapter.MiembroViewHolder>() {

    // 1. ViewHolder: Mantiene las referencias a las vistas de un solo ítem (item_miembro.xml)
    inner class MiembroViewHolder(val binding: ItemMiembroBinding) : RecyclerView.ViewHolder(binding.root)

    // 2. onCreateViewHolder: Se llama cuando el RecyclerView necesita crear un nuevo ViewHolder.
    // Infla el layout del ítem (item_miembro.xml) y lo devuelve.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiembroViewHolder {
        val binding = ItemMiembroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MiembroViewHolder(binding)
    }

    // 3. getItemCount: Devuelve el número total de ítems en la lista.
    override fun getItemCount(): Int {
        return miembros.size
    }

    // 4. onBindViewHolder: Conecta los datos de un miembro específico con las vistas del ViewHolder.
    // Se llama para cada ítem visible en la pantalla.
    override fun onBindViewHolder(holder: MiembroViewHolder, position: Int) {
        val miembroActual = miembros[position] // Obtiene el miembro de la lista

        holder.binding.apply {
            // Asigna los datos a las vistas correspondientes
            tvNombreMiembro.text = miembroActual.nombre
            tvIdMiembro.text = "ID: ${miembroActual.id}"

            // Usamos Glide para cargar la imagen desde una URL en el ImageView
            Glide.with(holder.itemView.context)
                .load(miembroActual.urlFoto)
                .circleCrop() // Para que la imagen sea circular
                .into(ivProfile)
        }
    }
}

*/
