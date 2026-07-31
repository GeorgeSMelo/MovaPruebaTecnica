package com.example.movapruebatecnica.crearPagos.domain.usesCases

import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.crearPagos.domain.model.PagoCreadoDomain
import com.example.movapruebatecnica.crearPagos.domain.model.RealizarPagoDomain
import com.example.movapruebatecnica.crearPagos.domain.repository.CrearPagoRemoteRepositoryInterface
import javax.inject.Inject

class CreatePaymentUseCase @Inject constructor(
    private val crearPagoRemoteRepositoryInterface: CrearPagoRemoteRepositoryInterface
) {
    suspend operator fun invoke(crearPago: RealizarPagoDomain) : ApiResponseStatus<PagoCreadoDomain> =
        crearPagoRemoteRepositoryInterface.processPayment(crearPago)
}