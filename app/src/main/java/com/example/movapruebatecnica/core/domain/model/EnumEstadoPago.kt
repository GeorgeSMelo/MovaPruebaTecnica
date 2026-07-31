package com.example.movapruebatecnica.core.domain.model

enum class EnumEstadoPago {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED;

    companion object {
        fun obtenerRandomState(): EnumEstadoPago {
            return entries.random()
        }
    }
}