package com.example.movapruebatecnica.core.data.hardware

import com.example.movapruebatecnica.core.domain.hardware.ReceiptPrinterInterface
import com.example.movapruebatecnica.core.domain.model.PrintResult
import com.example.movapruebatecnica.core.domain.model.ReceiptDomain
import kotlinx.coroutines.delay
import javax.inject.Inject

class ReceiptPrinter @Inject constructor(): ReceiptPrinterInterface {

    val siempreFalla = false

    override suspend fun print(receipt: ReceiptDomain): PrintResult {
        delay(3000)

        return if (siempreFalla) {
            PrintResult.Error("Fallo la impresión. Intente de nuevo")
        } else {
            PrintResult.Success
        }
    }
}