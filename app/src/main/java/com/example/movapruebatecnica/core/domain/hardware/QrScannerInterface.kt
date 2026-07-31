package com.example.movapruebatecnica.core.domain.hardware

import com.example.movapruebatecnica.core.domain.model.QrResult

interface QrScannerInterface {
    suspend fun scan(): QrResult
}