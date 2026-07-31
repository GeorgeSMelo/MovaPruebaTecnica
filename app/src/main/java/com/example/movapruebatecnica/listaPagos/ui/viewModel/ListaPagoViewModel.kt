package com.example.movapruebatecnica.listaPagos.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.core.domain.model.EnumEstadoSincronizacion
import com.example.movapruebatecnica.listaPagos.domain.usesCases.ObtenerHistoricoDePagosUseCase
import com.example.movapruebatecnica.listaPagos.domain.usesCases.SincronizarPagosLocalesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ListaPagoViewModel @Inject constructor(
    private val obtenerHistoricoDePagosUseCase: ObtenerHistoricoDePagosUseCase,
    private val sincronizarPagosLocalesUseCase: SincronizarPagosLocalesUseCase
): ViewModel(){

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _estadosSeleccionados = MutableStateFlow<Set<EnumEstadoSincronizacion>>(emptySet())
    val estadosSeleccionados: StateFlow<Set<EnumEstadoSincronizacion>> = _estadosSeleccionados.asStateFlow()

    private val _historicoPagos = MutableStateFlow<List<DetallePagoDomain>>(emptyList())
    val historicoPagos: StateFlow<List<DetallePagoDomain>> = combine(
        _historicoPagos,
        _estadosSeleccionados
    ) { pagos, estadosSincronizacion ->
        if (estadosSincronizacion.isEmpty()) {
            pagos
        } else {
            pagos.filter { it.syncStatus in estadosSincronizacion }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun obtenerHistoricoPagos() = viewModelScope.launch {
        _isLoading.value = true

        val listaDePagos = obtenerHistoricoDePagosUseCase()
        _historicoPagos.value = listaDePagos

        _isLoading.value = false
    }

    fun sincronizarPagosLocales() = viewModelScope.launch {
        _isLoading.value = true

        sincronizarPagosLocalesUseCase()
        val listaDePagos = obtenerHistoricoDePagosUseCase()
        _historicoPagos.value = listaDePagos


        _isLoading.value = false
    }

    fun actualizarFiltrosEstadoSincronizacion(estadoSincronizacion: EnumEstadoSincronizacion) {
        val estadosActuales = _estadosSeleccionados.value.toMutableSet()
        if (estadosActuales.contains(estadoSincronizacion)) {
            estadosActuales.remove(estadoSincronizacion)
        } else {
            estadosActuales.add(estadoSincronizacion)
        }
        _estadosSeleccionados.value = estadosActuales
    }
}