package com.example.movapruebatecnica.crearPagos.domain.repository

import com.example.movapruebatecnica.core.database.roomdb.entities.PaymentEntity

interface CrearPagoLocalRepositoryInterface {
    suspend fun guardarPago(pagoEntity: PaymentEntity)
    suspend fun existsReferenceLocally(reference: String): Boolean
}