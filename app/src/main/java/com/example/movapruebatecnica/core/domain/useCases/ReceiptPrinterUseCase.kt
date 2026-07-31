package com.example.movapruebatecnica.core.domain.useCases

import com.example.movapruebatecnica.core.domain.hardware.ReceiptPrinterInterface
import com.example.movapruebatecnica.core.domain.model.PrintResult
import com.example.movapruebatecnica.core.domain.model.ReceiptDomain
import javax.inject.Inject

class ReceiptPrinterUseCase @Inject constructor(
    private val receiptPrinterInterface: ReceiptPrinterInterface
) {
    suspend operator fun invoke(datosRecibo: ReceiptDomain) : PrintResult {
        return receiptPrinterInterface.print(receipt = datosRecibo)
    }
}