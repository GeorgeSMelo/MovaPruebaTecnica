package com.example.movapruebatecnica.crearPagos.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movapruebatecnica.core.domain.model.EnumEstadoPago
import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.crearPagos.domain.model.CreatePaymentUiState
import com.example.movapruebatecnica.core.domain.model.EnumMetodoPago
import com.example.movapruebatecnica.crearPagos.domain.model.PagoCreadoDomain
import com.example.movapruebatecnica.crearPagos.domain.model.RealizarPagoDomain
import com.example.movapruebatecnica.crearPagos.domain.usesCases.CreatePaymentUseCase
import com.example.movapruebatecnica.crearPagos.domain.usesCases.GuardarPagoUseCase
import com.example.movapruebatecnica.crearPagos.domain.usesCases.ObtenerReferenciaUseCase
import com.example.movapruebatecnica.core.domain.model.EnumEstadoSincronizacion
import com.example.movapruebatecnica.core.domain.model.NfcPaymentResult
import com.example.movapruebatecnica.core.domain.useCases.EscanearQrUseCase
import com.example.movapruebatecnica.core.domain.useCases.NfcPaymentReaderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CrearPagoViewModel @Inject constructor(
    private val createPaymentUseCase: CreatePaymentUseCase,
    private val guardarPagoUseCase: GuardarPagoUseCase,
    private val obtenerReferenciaUseCase: ObtenerReferenciaUseCase,
    private val escanearQrUseCase: EscanearQrUseCase,
    private val nfcPaymentReaderUseCase: NfcPaymentReaderUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreatePaymentUiState())
    val uiState: StateFlow<CreatePaymentUiState> = _uiState.asStateFlow()

    private val _stateRealizarPago = MutableStateFlow<ApiResponseStatus<PagoCreadoDomain>?>(null)
    val stateRealizarPago = _stateRealizarPago.asStateFlow()

    private val _stateNfcPayment = MutableStateFlow<NfcPaymentResult?>(null)
    val stateNfcPayment = _stateNfcPayment.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun actualizarMonto(monto: String) {
        val sanitized = monto.filter { it.isDigit() || it == '.' }

        val amountBigDecimal = sanitized.toBigDecimalOrNull()
        val error = when {
            sanitized.isBlank() -> "El valor es obligatorio"
            amountBigDecimal == null || amountBigDecimal <= BigDecimal.ZERO -> "El valor debe ser mayor a cero"
            else -> null
        }

        _uiState.update { it.copy(monto = sanitized, montoMensajeError = error) }
    }

    fun onMethodSelected(method: EnumMetodoPago) {
        _uiState.update { it.copy(selectedMethod = method) }
    }

    fun realizarPago() = viewModelScope.launch {
        _isLoading.value = true
        val currentState = _uiState.value
        val referenciaExterna = _uiState.value.reference

        val idempotencyKey = "payment-$referenciaExterna"
        val pago = RealizarPagoDomain(
            externalReference = referenciaExterna,
            monto = currentState.monto,
            method = currentState.selectedMethod,
            idempotencyKey = idempotencyKey,
            status = EnumEstadoPago.obtenerRandomState()
        )

        val respuestaApi = createPaymentUseCase(
            crearPago = pago
        )

        if (respuestaApi is ApiResponseStatus.Success) {
            savePago(EnumEstadoSincronizacion.SYNCED, pago)
        } else {
            savePago(EnumEstadoSincronizacion.PENDING_SYNC, pago)
        }
        _stateRealizarPago.value = respuestaApi
        _isLoading.value = false
    }

    private suspend fun savePago(estadoSincronizacion: EnumEstadoSincronizacion, pago: RealizarPagoDomain) {
        guardarPagoUseCase(
            estadoSincronizacion = estadoSincronizacion,
            pago = pago)
    }

    fun getReferenciaPago() = viewModelScope.launch {
        _isLoading.value = true
        val referenciaExterna = obtenerReferenciaUseCase()

        _uiState.update { it.copy(reference = referenciaExterna) }
        _isLoading.value = false
    }

    fun iniciarEscaneoQr() = viewModelScope.launch {
        val resultado = escanearQrUseCase()

        resultado.onSuccess { qrModel ->
            actualizarMonto(qrModel.monto)
            EnumMetodoPago.entries.find {
                it.name.equals(qrModel.metodoPago, ignoreCase = true)
            }?.let { metodo ->
                onMethodSelected(metodo)
            }
        }.onFailure {
            _uiState.update { it.copy(mostrarErrorQrDialog = true) }
        }
    }

    fun ocultarAlertaErrorQr() {
        _uiState.update { it.copy(mostrarErrorQrDialog = false) }
    }

    fun procesarPagoNFC() = viewModelScope.launch {
        _isLoading.value = true

        val resultNfc = nfcPaymentReaderUseCase()
        if (resultNfc is NfcPaymentResult.Success) {
            actualizarMonto(resultNfc.monto)
            EnumMetodoPago.entries.find {
                it.name.equals(resultNfc.metodoPago, ignoreCase = true)
            }?.let { metodo ->
                onMethodSelected(metodo)
            }
        }
        _stateNfcPayment.value = resultNfc

        _isLoading.value = false
    }

}