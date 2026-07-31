package com.example.movapruebatecnica.detallePagos.data.repository

import com.example.movapruebatecnica.core.data.model.RequestRealizarPagoDTO
import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.core.internet.MovApi
import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.detallePagos.domain.repository.DetallePagoRemoteRepositoryInterface
import com.example.movapruebatecnica.listaPagos.data.model.fromResponseDetalleDePagoDTOToDetallePagoDomain
import javax.inject.Inject

class DetallePagoRemoteRepository @Inject constructor(
    private val movApi: MovApi
) : DetallePagoRemoteRepositoryInterface {

    override suspend fun obtenerDetalleDePago(referencia: String): ApiResponseStatus<DetallePagoDomain> {
        return try {
            val call = movApi.getDetalleDePago(
                referencia = referencia
            )
            if (call.isSuccessful){
                val bodyReponse = call.body() ?: return ApiResponseStatus.Error("Error")
                return ApiResponseStatus.Success(
                    data = bodyReponse.fromResponseDetalleDePagoDTOToDetallePagoDomain()
                )
            }
            ApiResponseStatus.Error("Error")
        } catch (e: Exception){
            ApiResponseStatus.Error("")
        }
    }

    override suspend fun sincronizarPagoPendiente(pagoPorSincronizar: RequestRealizarPagoDTO): ApiResponseStatus<Unit> {
        return try {
            val call = movApi.processPayment(body = pagoPorSincronizar)
            ApiResponseStatus.Success(data = Unit)
        } catch (e : Exception) {
            ApiResponseStatus.Error("")
        }
    }
}