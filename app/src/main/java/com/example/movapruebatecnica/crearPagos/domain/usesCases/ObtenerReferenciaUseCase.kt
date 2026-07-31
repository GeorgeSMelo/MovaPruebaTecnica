package com.example.movapruebatecnica.crearPagos.domain.usesCases

import com.example.movapruebatecnica.crearPagos.domain.repository.CrearPagoLocalRepositoryInterface
import java.util.UUID
import javax.inject.Inject

class ObtenerReferenciaUseCase @Inject constructor(
    private val crearPagoLocalRepositoryInterface: CrearPagoLocalRepositoryInterface
) {
    suspend operator fun invoke() : String {
        val referencia = UUID.randomUUID().toString()
        val existeReferenciaLocal = crearPagoLocalRepositoryInterface.existsReferenceLocally(referencia)
        return if (!existeReferenciaLocal) {
            referencia
        } else {
            invoke()
        }
    }
}