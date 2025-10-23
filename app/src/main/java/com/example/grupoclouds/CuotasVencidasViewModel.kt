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
import java.time.temporal.WeekFields
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
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

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
            val hoy = LocalDate.now()

            // CAMBIO 3: La variable 'resultado' ahora es de tipo List<SocioConDetalles>.
            // El resto de la lógica es PERFECTA, ya que los métodos del DAO que llamas
            // son los correctos y devuelven el tipo que ahora esperamos.
            val resultado: List<SocioConDetalles> = when (filtroActual) {
                FiltroCuota.TODAS -> {
                    socioDao.getTodosLosSociosPorBusqueda(busquedaActual)
                }
                FiltroCuota.VENCIDAS -> {
                    socioDao.getSociosPorVencimientoYBusqueda(busquedaActual, "1900-01-01", hoy.minusDays(1).format(formatter))
                }
                FiltroCuota.VENCEN_HOY -> {
                    socioDao.getSociosPorVencimientoYBusqueda(busquedaActual, hoy.format(formatter), hoy.format(formatter))
                }
                FiltroCuota.VENCEN_SEMANA -> {
                    val finDeSemana = hoy.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 7)
                    socioDao.getSociosPorVencimientoYBusqueda(busquedaActual, hoy.format(formatter), finDeSemana.format(formatter))
                }
            }
            _socios.postValue(resultado)
            _cargando.postValue(false)
        }
    }
}

