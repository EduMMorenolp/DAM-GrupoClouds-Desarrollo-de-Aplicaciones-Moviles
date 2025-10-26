package com.example.grupoclouds

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.grupoclouds.db.AppDatabase
import com.example.grupoclouds.db.model.SocioConDetalles
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// La definición del enum es correcta.
enum class FiltroCuota {
    TODAS,
    VENCIDAS,
    VENCEN_HOY,
    VENCEN_SEMANA
}

class CuotasVencidasViewModel(application: Application) : AndroidViewModel(application) {

    // He usado AppDatabase.getInstance() que es más estándar, pero getDatabase() también funciona.
    private val socioDao = AppDatabase.getInstance(application).socioDao()
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // CAMBIO 2: El LiveData ahora contendrá una lista de SocioConDetalles.
    private val _socios = MutableLiveData<List<SocioConDetalles>>()
    val socios: LiveData<List<SocioConDetalles>> = _socios

    // El LiveData para el estado de carga es correcto.
    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> = _cargando

    private var filtroActual = FiltroCuota.TODAS
    private var busquedaActual = ""

    init {
        cargarSocios()
    }

    fun aplicarFiltro(filtro: FiltroCuota) {
        filtroActual = filtro
        cargarSocios()
    }

    fun aplicarBusqueda(query: String) {
        busquedaActual = query
        cargarSocios()
    }

    private fun cargarSocios() {
        _cargando.value = true
        viewModelScope.launch {
            val hoyCal = Calendar.getInstance()
            val hoyStr = formatter.format(hoyCal.time)

            // CAMBIO 3: La variable 'resultado' ahora es de tipo List<SocioConDetalles>.
            val resultado: List<SocioConDetalles> = when (filtroActual) {
                FiltroCuota.TODAS -> {
                    socioDao.getTodosLosSociosPorBusqueda(busquedaActual)
                }
                FiltroCuota.VENCIDAS -> {
                    // fecha hasta ayer
                    val ayerCal = Calendar.getInstance()
                    ayerCal.add(Calendar.DAY_OF_YEAR, -1)
                    val ayerStr = formatter.format(ayerCal.time)
                    socioDao.getSociosPorVencimientoYBusqueda(busquedaActual, "1900-01-01", ayerStr)
                }
                FiltroCuota.VENCEN_HOY -> {
                    socioDao.getSociosPorVencimientoYBusqueda(busquedaActual, hoyStr, hoyStr)
                }
                FiltroCuota.VENCEN_SEMANA -> {
                    val finDeSemanaCal = Calendar.getInstance()
                    finDeSemanaCal.add(Calendar.DAY_OF_YEAR, 7) // 7 días a partir de hoy
                    val finDeSemanaStr = formatter.format(finDeSemanaCal.time)
                    socioDao.getSociosPorVencimientoYBusqueda(busquedaActual, hoyStr, finDeSemanaStr)
                }
            }
            _socios.postValue(resultado)
            _cargando.postValue(false)
        }
    }
}
