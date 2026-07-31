package com.example.movapruebatecnica.detallePagos.domain.usesCases

import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.detallePagos.domain.repository.DetallePagoLocalRepositoryInterface
import javax.inject.Inject

class ObtenerDetalleDePagoLocalUseCase @Inject constructor(
    private val obtenerPagosLocalRepositoryInterface: DetallePagoLocalRepositoryInterface
) {
    suspend operator fun invoke(referencia: String): DetallePagoDomain? {
        return obtenerPagosLocalRepositoryInterface.getDetallePago(referencia = referencia)
    }
}