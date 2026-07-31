package com.example.movapruebatecnica.detallePagos.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.core.domain.model.PrintResult
import com.example.movapruebatecnica.core.domain.model.fromDetallePagoDomianToReceiptDomain
import com.example.movapruebatecnica.core.domain.useCases.ReceiptPrinterUseCase
import com.example.movapruebatecnica.detallePagos.domain.usesCases.ObtenerDetalleDePagoLocalUseCase
import com.example.movapruebatecnica.detallePagos.domain.usesCases.ObtenerDetalleDePagoRemotoUseCase
import com.example.movapruebatecnica.detallePagos.domain.usesCases.SincronizarPagoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetallePagoViewModel @Inject constructor(
    private val obtenerDetalleDePagoLocalUseCase: ObtenerDetalleDePagoLocalUseCase,
    private val obtenerDetalleDePagoRemotoUseCase: ObtenerDetalleDePagoRemotoUseCase,
    private val sincronizarPagoUseCase: SincronizarPagoUseCase,
    private val receiptPrinterUseCase: ReceiptPrinterUseCase
): ViewModel(){

    private val _detalleDePago = MutableStateFlow<DetallePagoDomain?>(null)
    val detalleDePago = _detalleDePago.asStateFlow()

    private val _stateImpresion = MutableStateFlow<PrintResult?>(null)
    val stateImpresion = _stateImpresion.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun obtenerDatosDeDetallePago(referencia: String) = viewModelScope.launch {
        _isLoading.value = true
        val detallePagoLocal = obtenerDetalleDePagoLocalUseCase(
            referencia = referencia
        )

        if (detallePagoLocal != null) {
            _detalleDePago.value = detallePagoLocal
            _isLoading.value = false
            return@launch
        }

        val apiResponse = obtenerDetalleDePagoRemotoUseCase(
            referencia = referencia
        )

        if (apiResponse is ApiResponseStatus.Success) {
            _detalleDePago.value = apiResponse.data
        }
        _isLoading.value = false
    }

    fun sincronizarPago() = viewModelScope.launch {
        _isLoading.value = true

        sincronizarPagoUseCase(_detalleDePago.value)

        val detallePagoLocal = obtenerDetalleDePagoLocalUseCase(
            referencia = _detalleDePago.value?.reference ?: ""
        )
        _detalleDePago.value = detallePagoLocal

        _isLoading.value = false
    }

    fun imprimirDetallePago() = viewModelScope.launch {
        _isLoading.value = true

        _stateImpresion.value = receiptPrinterUseCase(
            datosRecibo = _detalleDePago.value.fromDetallePagoDomianToReceiptDomain()
        )

        _isLoading.value = false
    }
}