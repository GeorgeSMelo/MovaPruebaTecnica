package com.example.movapruebatecnica.crearPagos.data.repository

import com.example.movapruebatecnica.core.database.roomdb.daos.PaymentDAO
import com.example.movapruebatecnica.core.database.roomdb.entities.PaymentEntity
import com.example.movapruebatecnica.crearPagos.domain.repository.CrearPagoLocalRepositoryInterface
import javax.inject.Inject

class CrearPagoLocalRepository @Inject constructor(
    private val paymentDAO: PaymentDAO
) : CrearPagoLocalRepositoryInterface {
    override suspend fun guardarPago(pagoEntity: PaymentEntity) {
        paymentDAO.insertPayment(pagoEntity)
    }

    override suspend fun existsReferenceLocally(reference: String): Boolean {
        return paymentDAO.existsByReference(reference)
    }
}