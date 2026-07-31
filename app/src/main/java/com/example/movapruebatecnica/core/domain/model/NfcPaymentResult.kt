package com.example.movapruebatecnica.core.domain.model

sealed class NfcPaymentResult {
    data class Success(val monto: String, val metodoPago: String) : NfcPaymentResult()
    data class Error(val message: String) : NfcPaymentResult()
    object ReadingCancelled : NfcPaymentResult()
}