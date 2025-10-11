package com.example.grupoclouds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grupoclouds.db.dao.SocioDao
import com.example.grupoclouds.db.model.SocioConDetalles
import com.example.grupoclouds.util.ConstantesPago
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AlertaVencimientoViewModel(private val socioDao: SocioDao) : ViewModel() {

    // Expone el Flow de socios por vencer a la UI.
    val sociosPorVencer: Flow<List<SocioConDetalles>>

    init {
        // Obtiene la fecha actual y la fecha límite para la alerta.
        val fechaHoy = obtenerFechaActualFormateada()
        val fechaLimite = ConstantesPago.calcularFechaLimiteAlerta(ConstantesPago.DIAS_ALERTA_VENCIMIENTO)
        
        // Llama al método del DAO con el rango de fechas para inicializar el Flow.
        sociosPorVencer = socioDao.obtenerSociosPorVencer(fechaHoy, fechaLimite)
    }

    private fun obtenerFechaActualFormateada(): String {
        val fechaActual = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return fechaActual.format(formatter)
    }
}

/**
 * Factory para crear la instancia de AlertaVencimientoViewModel con sus dependencias.
 */
class AlertaVencimientoViewModelFactory(private val socioDao: SocioDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertaVencimientoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlertaVencimientoViewModel(socioDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
