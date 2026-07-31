package com.example.movapruebatecnica.crearPagos.domain.repository

import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.crearPagos.domain.model.PagoCreadoDomain
import com.example.movapruebatecnica.crearPagos.domain.model.RealizarPagoDomain

interface CrearPagoRemoteRepositoryInterface {
    suspend fun processPayment(request: RealizarPagoDomain): ApiResponseStatus<PagoCreadoDomain>
}