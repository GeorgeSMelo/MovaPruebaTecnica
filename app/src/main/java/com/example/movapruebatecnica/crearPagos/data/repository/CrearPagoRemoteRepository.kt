package com.example.movapruebatecnica.crearPagos.data.repository


import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.core.internet.MovApi
import com.example.movapruebatecnica.crearPagos.data.model.fromResponseCrearDatoDTOToPagoCreadoDomain
import com.example.movapruebatecnica.crearPagos.domain.model.PagoCreadoDomain
import com.example.movapruebatecnica.crearPagos.domain.model.RealizarPagoDomain
import com.example.movapruebatecnica.crearPagos.domain.model.fromRealizarPagoDomainToRequestRealizarPagoDTO
import com.example.movapruebatecnica.crearPagos.domain.repository.CrearPagoRemoteRepositoryInterface
import javax.inject.Inject

class CrearPagoRemoteRepository @Inject constructor(
    private val movApi: MovApi
) : CrearPagoRemoteRepositoryInterface {

    override suspend fun processPayment(request: RealizarPagoDomain): ApiResponseStatus<PagoCreadoDomain> {
        return try {
            val call = movApi.processPayment(
                request.fromRealizarPagoDomainToRequestRealizarPagoDTO()
            )
            if (call.isSuccessful) {
                val respuestaBody =
                    call.body() ?: return ApiResponseStatus.Error("Error")
                return ApiResponseStatus.Success(data = respuestaBody.fromResponseCrearDatoDTOToPagoCreadoDomain())
            }

            ApiResponseStatus.Error("")
        } catch (e: Throwable) {
            ApiResponseStatus.Error("")
        }
    }
}