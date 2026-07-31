package com.example.movapruebatecnica.listaPagos.domain.repository

import com.example.movapruebatecnica.core.data.model.RequestRealizarPagoDTO
import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.core.internet.ApiResponseStatus

interface ListaPagoRemoteRepositoryInterface{
    suspend fun obtenerHistoricoDePagos(): ApiResponseStatus<List<DetallePagoDomain>>

    suspend fun sincronizarPagosPendientes(
        pagosPorSincronizar: List<RequestRealizarPagoDTO>
    ) : ApiResponseStatus<Unit>
}