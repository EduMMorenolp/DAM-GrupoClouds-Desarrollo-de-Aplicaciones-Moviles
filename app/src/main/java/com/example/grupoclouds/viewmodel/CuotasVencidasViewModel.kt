package com.example.grupoclouds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grupoclouds.db.dao.SocioDao
import com.example.grupoclouds.db.model.SocioConDetalles
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CuotasVencidasViewModel(private val socioDao: SocioDao) : ViewModel() {

    // Expone el Flow de socios vencidos a la UI.
    val sociosVencidos: Flow<List<SocioConDetalles>>

    init {
        // Obtiene la fecha actual y la formatea.
        val fechaActualFormateada = obtenerFechaActualFormateada()
        // Llama al método del DAO con la fecha actual para inicializar el Flow.
        sociosVencidos = socioDao.obtenerSociosVencidos(fechaActualFormateada)
    }

    /**
     * Función privada para obtener la fecha de hoy en el formato requerido por la consulta SQL.
     */
    private fun obtenerFechaActualFormateada(): String {
        val fechaActual = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return fechaActual.format(formatter)
    }
}

/**
 * Factory para crear la instancia de CuotasVencidasViewModel con sus dependencias (SocioDao).
 */
class CuotasVencidasViewModelFactory(private val socioDao: SocioDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CuotasVencidasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CuotasVencidasViewModel(socioDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
