package com.example.movapruebatecnica.detallePagos.domain.usesCases

import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.detallePagos.domain.repository.DetallePagoRemoteRepositoryInterface
import javax.inject.Inject

class ObtenerDetalleDePagoRemotoUseCase @Inject constructor(
    private val detallePagoRemoteRepositoryInterface: DetallePagoRemoteRepositoryInterface
) {
    suspend operator fun invoke(referencia: String) : ApiResponseStatus<DetallePagoDomain> =
        detallePagoRemoteRepositoryInterface.obtenerDetalleDePago(
            referencia = referencia
        )
}