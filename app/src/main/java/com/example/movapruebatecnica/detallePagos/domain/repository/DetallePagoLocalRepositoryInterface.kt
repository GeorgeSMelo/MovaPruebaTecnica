package com.example.movapruebatecnica.detallePagos.domain.repository

import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain

interface DetallePagoLocalRepositoryInterface {
    suspend fun getDetallePago(referencia: String) : DetallePagoDomain?

    suspend fun actualizarEstadoSincronizacionPago(
        referencia: String,
        nuevoEstadoSincronizacion : String
    )
}