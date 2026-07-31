package com.example.movapruebatecnica.core.domain.model

sealed interface QrResult {
    data class Success(val data: String) : QrResult
    data class Error(val mensaje: String) : QrResult
    data object Cancelled : QrResult
}