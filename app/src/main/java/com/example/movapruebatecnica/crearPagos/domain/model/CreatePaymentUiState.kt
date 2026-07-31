package com.example.movapruebatecnica.crearPagos.domain.model

import com.example.movapruebatecnica.core.domain.model.EnumMetodoPago

data class CreatePaymentUiState(
    val monto: String = "",
    val selectedMethod: EnumMetodoPago = EnumMetodoPago.CARD,
    val montoMensajeError: String? = null,
    val reference: String = "",
    val mostrarErrorQrDialog: Boolean = false
) {
    val isValid: Boolean
        get() = montoMensajeError == null && monto.isNotEmpty()
}
