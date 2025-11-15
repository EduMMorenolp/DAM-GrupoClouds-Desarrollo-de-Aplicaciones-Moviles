package com.example.grupoclouds.util

import java.text.SimpleDateFormat
import java.util.*

object ConstantesPago {
    const val DIAS_PAGO_30 = 30  // Para cuota mensual
    const val DIAS_PAGO_365 = 365  // Para cuota anual

    fun calcularFechaVencimiento(dias: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, dias)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(calendar.time)
    }
}

