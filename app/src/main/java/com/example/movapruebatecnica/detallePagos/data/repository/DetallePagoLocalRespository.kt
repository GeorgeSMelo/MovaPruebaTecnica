package com.example.movapruebatecnica.detallePagos.data.repository

import com.example.movapruebatecnica.core.database.roomdb.daos.PaymentDAO
import com.example.movapruebatecnica.core.database.roomdb.entities.fromPaymentEntityToDetallePagoDomain
import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.detallePagos.domain.repository.DetallePagoLocalRepositoryInterface
import javax.inject.Inject

class DetallePagoLocalRespository @Inject constructor(
    private val paymentDAO: PaymentDAO
) : DetallePagoLocalRepositoryInterface {
    override suspend fun getDetallePago(referencia: String): DetallePagoDomain? {
        val pagoLocal = paymentDAO.getPagoLocal(
            referencia = referencia
        ) ?: return null
        return pagoLocal.fromPaymentEntityToDetallePagoDomain()
    }

    override suspend fun actualizarEstadoSincronizacionPago(
        referencia: String,
        nuevoEstadoSincronizacion: String
    ) {
        paymentDAO.actualizarEstadoSincronizacionDePago(
            referencia = referencia,
            estadoSincronizacion = nuevoEstadoSincronizacion
        )
    }

}