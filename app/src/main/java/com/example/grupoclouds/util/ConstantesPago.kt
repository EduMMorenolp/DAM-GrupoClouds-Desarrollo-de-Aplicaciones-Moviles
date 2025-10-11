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

    /**
     * 2.4: Calcula una fecha de vencimiento sumando una cantidad de días a la fecha actual.
     *
     * @param diasParaSumar La cantidad de días a sumar (ej. DIAS_PAGO_15 o DIAS_PAGO_30).
     * @return Un String con la fecha de vencimiento en formato "YYYY-MM-DD".
     */
    fun calcularFechaVencimiento(diasParaSumar: Int): String {
        // Obtiene la fecha actual usando la API moderna de Java 8
        val fechaActual = LocalDate.now()

        // Suma los días especificados. plusDays devuelve un nuevo objeto LocalDate.
        val fechaVencimiento = fechaActual.plusDays(diasParaSumar.toLong())

        // Define el formato de salida deseado
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // Formatea y retorna la fecha como un String
        return fechaVencimiento.format(formatter)
    }
}
