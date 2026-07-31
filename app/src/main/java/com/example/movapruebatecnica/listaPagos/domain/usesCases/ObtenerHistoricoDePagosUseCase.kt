package com.example.movapruebatecnica.listaPagos.domain.usesCases

import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.listaPagos.domain.repository.ListaPagoLocalRepositoryInterface
import com.example.movapruebatecnica.listaPagos.domain.repository.ListaPagoRemoteRepositoryInterface
import javax.inject.Inject

class ObtenerHistoricoDePagosUseCase @Inject constructor(
    private val listaPagoRemoteRepositoryInterface: ListaPagoRemoteRepositoryInterface,
    private val listaPagoLocalRepositoryInterface: ListaPagoLocalRepositoryInterface
) {
    suspend operator fun invoke() : List<DetallePagoDomain> {

        val historicoPagosApi = mutableListOf<DetallePagoDomain>()
        val respuestaHistoricoPagosApi = listaPagoRemoteRepositoryInterface.obtenerHistoricoDePagos()

        if (respuestaHistoricoPagosApi is ApiResponseStatus.Success) {
            historicoPagosApi.addAll(respuestaHistoricoPagosApi.data)
        }

        val historicoPagosBaseDatos = listaPagoLocalRepositoryInterface.obtenerHistoricoDePagos()

        val historicoPagosUnificados = (historicoPagosBaseDatos + historicoPagosApi)
            .associateBy { it.reference }
            .values
            .toList()

        return historicoPagosUnificados.sortedByDescending { it.createdAt }
    }
}