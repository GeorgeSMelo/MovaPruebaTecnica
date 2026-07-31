package com.example.movapruebatecnica.detallePagos.domain.usesCases

import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.core.domain.model.EnumEstadoSincronizacion
import com.example.movapruebatecnica.core.domain.model.fromDetallePagoDomainToRequestRealizarPagoDTO
import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.detallePagos.domain.repository.DetallePagoLocalRepositoryInterface
import com.example.movapruebatecnica.detallePagos.domain.repository.DetallePagoRemoteRepositoryInterface
import javax.inject.Inject

class SincronizarPagoUseCase @Inject constructor(
    private val detallePagoLocalRepositoryInterface: DetallePagoLocalRepositoryInterface,
    private val detallePagoRemoteRepositoryInterface: DetallePagoRemoteRepositoryInterface
) {
    suspend operator fun invoke(
        detallePago: DetallePagoDomain?
    ) {
        val dataPago = detallePago ?: return

        val apiResponse = detallePagoRemoteRepositoryInterface.sincronizarPagoPendiente(
            pagoPorSincronizar = dataPago.fromDetallePagoDomainToRequestRealizarPagoDTO()
        )

        if (apiResponse is ApiResponseStatus.Success) {
            detallePagoLocalRepositoryInterface.actualizarEstadoSincronizacionPago(
                referencia = dataPago.reference,
                nuevoEstadoSincronizacion = EnumEstadoSincronizacion.SYNCED.name
            )
        } else {
            detallePagoLocalRepositoryInterface.actualizarEstadoSincronizacionPago(
                referencia = dataPago.reference,
                nuevoEstadoSincronizacion = EnumEstadoSincronizacion.SYNC_ERROR.name
            )
        }
    }
}
