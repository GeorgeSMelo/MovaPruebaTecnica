package com.example.movapruebatecnica.listaPagos.data.repository

import com.example.movapruebatecnica.core.data.model.RequestRealizarPagoDTO
import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.core.internet.MovApi
import com.example.movapruebatecnica.listaPagos.domain.repository.ListaPagoRemoteRepositoryInterface
import javax.inject.Inject

class ListaPagoRemoteRepository @Inject constructor(
    private val movApi: MovApi
) : ListaPagoRemoteRepositoryInterface {
    override suspend fun obtenerHistoricoDePagos(): ApiResponseStatus<List<DetallePagoDomain>> {
        try {
            /*val call = movApi.getDetalleDePago()
            if (call.isSuccessful){
                val listaPagos = call.body() ?: return ApiResponseStatus.Error("Error")
                return ApiResponseStatus.Success(data = listaPagos.extendsResponseListListaPagoDTOToListListaPagoDomain())
            } else {
                return ApiResponseStatus.Error("Error")
            }*/
            return ApiResponseStatus.Error("Error")
        } catch (e: Exception){
            return ApiResponseStatus.Error("")
        }
    }

    override suspend fun sincronizarPagosPendientes(pagosPorSincronizar: List<RequestRealizarPagoDTO>): ApiResponseStatus<Unit> {
        return try {
            val call = movApi.sincronizarPagosLocales(pagosPorSincronizar)
            ApiResponseStatus.Success(data = Unit)
        } catch (e: Exception) {
            ApiResponseStatus.Error("")
        }
    }

}