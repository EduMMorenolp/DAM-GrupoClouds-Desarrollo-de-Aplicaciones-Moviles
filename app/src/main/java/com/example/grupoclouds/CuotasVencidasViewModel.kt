package com.example.grupoclouds

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.grupoclouds.db.AppDatabase
import com.example.grupoclouds.db.model.SocioConDetalles
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// La definición del enum es correcta.
enum class FiltroCuota {
    TODAS,
    VENCIDAS,
    VENCEN_HOY,
    VENCEN_SEMANA
}

class CuotasVencidasViewModel(application: Application) : AndroidViewModel(application) {

    private val socioDao = AppDatabase.getInstance(application).socioDao()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val _socios = MutableLiveData<List<SocioConDetalles>>()
    val socios: LiveData<List<SocioConDetalles>> = _socios

    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> = _cargando

    private var filtroActual = FiltroCuota.TODAS
    private var busquedaActual = ""

    // Almacenamos la lista completa de la BD para filtrar en memoria
    private var listaCompletaDeSocios = listOf<SocioConDetalles>()

    init {
        cargarSociosDesdeDB()
    }

    private fun cargarSociosDesdeDB() {
        _cargando.value = true
        viewModelScope.launch {
            // Obtenemos la lista una sola vez usando la nueva función optimizada
            listaCompletaDeSocios = socioDao.getSociosConCuotasVencidasOProximas()
            // Una vez cargada, aplicamos el filtro y búsqueda inicial
            aplicarFiltrosYBusqueda()
            _cargando.postValue(false)
        }
    }

    fun aplicarFiltro(filtro: FiltroCuota) {
        filtroActual = filtro
        aplicarFiltrosYBusqueda()
    }

    fun aplicarBusqueda(query: String) {
        busquedaActual = query.trim()
        aplicarFiltrosYBusqueda()
    }

    private fun aplicarFiltrosYBusqueda() {
        // 1. Aplicamos la búsqueda sobre la lista completa
        val sociosBuscados = if (busquedaActual.isBlank()) {
            listaCompletaDeSocios
        } else {
            listaCompletaDeSocios.filter {
                it.nombre.contains(busquedaActual, ignoreCase = true) ||
                        it.apellido?.contains(busquedaActual, ignoreCase = true) == true ||
                        it.dni.contains(busquedaActual, ignoreCase = true)
            }
        }

        // 2. Aplicamos el filtro sobre el resultado de la búsqueda
        val hoy = LocalDate.now()
        val resultadoFinal = when (filtroActual) {
            FiltroCuota.TODAS -> {
                sociosBuscados
            }
            FiltroCuota.VENCIDAS -> {
                sociosBuscados.filter {
                    it.cuota_hasta == null || tryParseDate(it.cuota_hasta)?.isBefore(hoy) == true
                }
            }
            FiltroCuota.VENCEN_HOY -> {
                sociosBuscados.filter {
                    tryParseDate(it.cuota_hasta)?.isEqual(hoy) == true
                }
            }
            FiltroCuota.VENCEN_SEMANA -> {
                val finDeSemana = hoy.plusDays(7)
                sociosBuscados.filter {
                    val fecha = tryParseDate(it.cuota_hasta)
                    fecha != null && !fecha.isBefore(hoy) && !fecha.isAfter(finDeSemana)
                }
            }
        }
        _socios.postValue(resultadoFinal)
    }

    private fun tryParseDate(dateStr: String?): LocalDate? {
        return if (dateStr.isNullOrEmpty()) {
            null
        } else {
            try {
                LocalDate.parse(dateStr, formatter)
            } catch (e: DateTimeParseException) {
                null
            }
        }
    }
}