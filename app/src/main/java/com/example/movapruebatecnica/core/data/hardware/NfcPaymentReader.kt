package com.example.movapruebatecnica.core.data.hardware

import com.example.movapruebatecnica.core.domain.hardware.NfcPaymentReaderInterface
import com.example.movapruebatecnica.core.domain.model.EnumMetodoPago
import com.example.movapruebatecnica.core.domain.model.NfcPaymentResult
import kotlinx.coroutines.delay
import javax.inject.Inject

class NfcPaymentReader @Inject constructor(): NfcPaymentReaderInterface {
    var siempreFalla: Boolean = true
    override suspend fun readPayment(): NfcPaymentResult {
        delay(3000)

        return if (siempreFalla) {
            NfcPaymentResult.Error("Error al leer la tarjeta. Intente de nuevo.")
        } else {
            NfcPaymentResult.Success(
                monto = "100",
                metodoPago = EnumMetodoPago.NFC.name
            )
        }
    }
}