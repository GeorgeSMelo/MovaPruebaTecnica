package com.example.movapruebatecnica.detallePagos.domain.repository

import com.example.movapruebatecnica.core.data.model.RequestRealizarPagoDTO
import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain

interface DetallePagoRemoteRepositoryInterface {
    suspend fun obtenerDetalleDePago(referencia: String): ApiResponseStatus<DetallePagoDomain>

    suspend fun sincronizarPagoPendiente(
        pagoPorSincronizar: RequestRealizarPagoDTO
    ) : ApiResponseStatus<Unit>
}