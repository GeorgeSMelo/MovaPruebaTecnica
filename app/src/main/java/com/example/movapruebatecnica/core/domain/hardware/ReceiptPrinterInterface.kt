package com.example.movapruebatecnica.core.domain.hardware

import com.example.movapruebatecnica.core.domain.model.PrintResult
import com.example.movapruebatecnica.core.domain.model.ReceiptDomain
import javax.inject.Inject

interface ReceiptPrinterInterface {
    suspend fun print(receipt: ReceiptDomain): PrintResult
}