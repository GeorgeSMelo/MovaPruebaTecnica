package com.example.movapruebatecnica.core.domain.hardware

import com.example.movapruebatecnica.core.domain.model.NfcPaymentResult

interface NfcPaymentReaderInterface {
    suspend fun readPayment(): NfcPaymentResult
}