package com.example.movapruebatecnica.core.domain.useCases

import com.example.movapruebatecnica.core.domain.hardware.NfcPaymentReaderInterface
import com.example.movapruebatecnica.core.domain.model.NfcPaymentResult
import javax.inject.Inject

class NfcPaymentReaderUseCase @Inject constructor(
    private val nfcPaymentReaderInterface: NfcPaymentReaderInterface
) {
    suspend operator fun invoke() : NfcPaymentResult =
        nfcPaymentReaderInterface.readPayment()
}