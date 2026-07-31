package com.example.movapruebatecnica.core.domain.model

sealed class PrintResult {
    data object Success : PrintResult()
    data class Error(val message: String) : PrintResult()
}