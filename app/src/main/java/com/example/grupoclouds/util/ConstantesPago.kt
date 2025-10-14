package com.example.grupoclouds.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Objeto Singleton para centralizar las constantes y la lógica de negocio relacionadas con los pagos.
 * Al ser un 'object', solo existirá una instancia de esta clase en toda la aplicación,
 * garantizando un único punto de acceso a estos valores.
 */
object ConstantesPago {

    // 2.2: Constantes para los precios de los planes
    const val VALOR_MONTO_15_DIAS: Float = 25.00f
    const val VALOR_MONTO_1_MES: Float = 50.00f

    // 2.3: Constantes para los días de vencimiento
    const val DIAS_PAGO_15: Int = 15
    const val DIAS_PAGO_30: Int = 30

    // 7.1: Constante para el umbral de alerta de vencimiento
    const val DIAS_ALERTA_VENCIMIENTO: Int = 7

    /**
     * 2.4: Calcula una fecha de vencimiento sumando una cantidad de días a la fecha actual.
     *
     * @param diasParaSumar La cantidad de días a sumar (ej. DIAS_PAGO_15 o DIAS_PAGO_30).
     * @return Un String con la fecha de vencimiento en formato "YYYY-MM-DD".
     */
    fun calcularFechaVencimiento(diasParaSumar: Int): String {
        val fechaActual = LocalDate.now()
        val fechaVencimiento = fechaActual.plusDays(diasParaSumar.toLong())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return fechaVencimiento.format(formatter)
    }

    /**
     * 7.2: Calcula la fecha límite para el período de alerta de vencimiento.
     *
     * @param diasDeAlerta Los días a sumar a la fecha actual para definir el límite superior.
     * @return Un String con la fecha límite en formato "YYYY-MM-DD".
     */
    fun calcularFechaLimiteAlerta(diasDeAlerta: Int): String {
        val fechaActual = LocalDate.now()
        val fechaLimite = fechaActual.plusDays(diasDeAlerta.toLong())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return fechaLimite.format(formatter)
    }
}
