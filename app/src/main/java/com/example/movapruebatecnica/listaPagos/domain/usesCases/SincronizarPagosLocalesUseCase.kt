package com.example.movapruebatecnica.listaPagos.domain.usesCases

import com.example.movapruebatecnica.core.domain.model.EnumEstadoSincronizacion
import com.example.movapruebatecnica.core.domain.model.fromListDetallePagoDomainToListRequestRealizarPagoDTO
import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.listaPagos.domain.repository.ListaPagoLocalRepositoryInterface
import com.example.movapruebatecnica.listaPagos.domain.repository.ListaPagoRemoteRepositoryInterface
import javax.inject.Inject

class SincronizarPagosLocalesUseCase @Inject constructor(
    private val listaPagoLocalRepositoryInterface: ListaPagoLocalRepositoryInterface,
    private val listaPagoRemoteRepositoryInterface: ListaPagoRemoteRepositoryInterface
) {
    suspend operator fun invoke() {
        val pagosLocalesSinSincronizar = listaPagoLocalRepositoryInterface.obtenerPagosPorEstadosDeSincronizacion(
            estados = listOf(EnumEstadoSincronizacion.PENDING_SYNC.name, EnumEstadoSincronizacion.SYNC_ERROR.name)
        )

        val apiResponse = listaPagoRemoteRepositoryInterface.sincronizarPagosPendientes(
            pagosPorSincronizar = pagosLocalesSinSincronizar.fromListDetallePagoDomainToListRequestRealizarPagoDTO()
        )
        val listaDeReferencias = pagosLocalesSinSincronizar.map { it.reference }

        if (apiResponse is ApiResponseStatus.Success) {
            listaPagoLocalRepositoryInterface.actualizarEstadoSincronizacionPagos(
                listadoReferencias = listaDeReferencias,
                nuevoEstadoSincronizacion = EnumEstadoSincronizacion.SYNCED.name
            )
        } else {
            listaPagoLocalRepositoryInterface.actualizarEstadoSincronizacionPagos(
                listadoReferencias = listaDeReferencias,
                nuevoEstadoSincronizacion = EnumEstadoSincronizacion.SYNC_ERROR.name
            )
        }
    }
}