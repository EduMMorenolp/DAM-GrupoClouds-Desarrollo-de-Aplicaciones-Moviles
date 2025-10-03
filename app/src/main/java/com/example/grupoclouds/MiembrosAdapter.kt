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

package com.example.grupoclouds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MiembrosAdapter(private val listaMiembros: List<Miembro>) :
    RecyclerView.Adapter<MiembrosAdapter.MiembroViewHolder>() {

    /**
     * El ViewHolder describe una vista de un ítem y sus metadatos dentro del RecyclerView.
     */
    inner class MiembroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Obtenemos las referencias a las vistas dentro de item_miembro.xml
        val imagenPerfil: ImageView = itemView.findViewById(R.id.iv_profile)
        val textoNombre: TextView = itemView.findViewById(R.id.tv_nombre_miembro)
        val textoId: TextView = itemView.findViewById(R.id.tv_id_miembro)
    }

    /**
     * Se llama cuando el RecyclerView necesita un nuevo ViewHolder para representar un ítem.
     * Aquí "inflamos" (creamos) la vista de la fila desde el XML.
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
     * Aquí conectamos los datos del 'Miembro' con las vistas del 'ViewHolder'.
     */
    override fun onBindViewHolder(holder: MiembroViewHolder, position: Int) {
        val miembroActual = listaMiembros[position]

        // Asignamos los datos del miembro a las vistas
        holder.textoNombre.text = miembroActual.nombre
        holder.textoId.text = "ID: ${miembroActual.idSocio}"

        // Usamos la librería Glide para cargar la imagen desde la URL de forma eficiente
        Glide.with(holder.itemView.context)
            .load(miembroActual.urlImagen)
            .circleCrop() // Aplica una máscara circular a la imagen
            .into(holder.imagenPerfil)
    }
}

