package com.example.grupoclouds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// La solución principal está en esta línea:
// Hereda de RecyclerView.Adapter e indica que usará 'ActividadViewHolder'
class ActividadesAdapter(private val actividades: List<Actividad>) :
    RecyclerView.Adapter<ActividadesAdapter.ActividadViewHolder>() {

    /**
     * Esta clase interna (ViewHolder) representa una única fila de tu lista.
     * Guarda las referencias a las vistas que están dentro de item_actividades.xml
     * para no tener que buscarlas cada vez (es más eficiente).
     */
    class ActividadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.text_nombreActividad)
        val instructorTextView: TextView = itemView.findViewById(R.id.text_nombreInstructor)
        val precioTextView: TextView = itemView.findViewById(R.id.text_precio)
    }

    /**
     * Este método se llama cuando el RecyclerView necesita crear una nueva "caja" (ViewHolder)
     * para una fila. Aquí es donde se "infla" (crea) la vista a partir de tu XML.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        // Crea la vista de la fila usando tu layout item_actividad.xml
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividades, parent, false)
        return ActividadViewHolder(view)
    }

    /**
     * Este método se llama para rellenar los datos de una fila específica (en la posición 'position').
     * Aquí es donde conectas tus datos (de la lista) con las vistas (del ViewHolder).
     */
    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        // Obtiene el objeto 'Actividad' de la lista para esta posición
        val actividad = actividades[position]

        // Asigna los datos del objeto a los TextViews de la fila
        holder.nombreTextView.text = actividad.nombre
        holder.instructorTextView.text = actividad.instructor
        holder.precioTextView.text = actividad.precio
    }

    /**
     * Este método simplemente devuelve el número total de elementos que hay en tu lista.
     * El RecyclerView lo usa para saber cuántas filas tiene que dibujar.
     */
    override fun getItemCount(): Int {
        return actividades.size
    }
}