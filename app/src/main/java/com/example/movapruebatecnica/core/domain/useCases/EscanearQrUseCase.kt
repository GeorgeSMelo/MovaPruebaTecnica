package com.example.movapruebatecnica.core.domain.useCases

import com.example.movapruebatecnica.core.domain.hardware.QrScannerInterface
import com.example.movapruebatecnica.core.domain.model.QrResult
import com.example.movapruebatecnica.crearPagos.domain.model.QrPagoDomain
import com.google.gson.Gson
import javax.inject.Inject

class EscanearQrUseCase @Inject constructor(
    private val gson: Gson,
    private val qrScannerInterface: QrScannerInterface
) {
    suspend operator fun invoke(): Result<QrPagoDomain> {
        return when (val result = qrScannerInterface.scan()) {
            is QrResult.Success -> parsearQr(result.data)
            is QrResult.Error -> Result.failure(Exception(result.mensaje))
            is QrResult.Cancelled -> Result.failure(Exception("Escaneo cancelado"))
        }
    }

    private fun parsearQr(jsonString: String?) : Result<QrPagoDomain> {
        return runCatching {
            require(!jsonString.isNullOrBlank()) { "Código QR vacío" }
            val qrData = gson.fromJson(jsonString, QrPagoDomain::class.java)

            requireNotNull(qrData) { "Error al deserializar el JSON" }

            require(qrData.monto.isNotBlank() && qrData.metodoPago.isNotBlank()) {
                "El QR debe contener un monto y un método de pago válidos"
            }

            qrData
        }
    }
}