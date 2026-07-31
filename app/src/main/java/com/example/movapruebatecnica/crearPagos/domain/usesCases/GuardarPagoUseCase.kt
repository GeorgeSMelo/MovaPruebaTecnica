package com.example.movapruebatecnica.crearPagos.domain.usesCases

import com.example.movapruebatecnica.crearPagos.domain.model.RealizarPagoDomain
import com.example.movapruebatecnica.crearPagos.domain.model.fromRealizarPagoDomianToPaymentEntity
import com.example.movapruebatecnica.crearPagos.domain.repository.CrearPagoLocalRepositoryInterface
import com.example.movapruebatecnica.core.domain.model.EnumEstadoSincronizacion
import javax.inject.Inject

class GuardarPagoUseCase @Inject constructor(
    private val crearPagoLocalRepositoryInterface: CrearPagoLocalRepositoryInterface
) {
    suspend operator fun invoke(estadoSincronizacion: EnumEstadoSincronizacion, pago: RealizarPagoDomain) {
        val savePago = pago.fromRealizarPagoDomianToPaymentEntity(
            estadoSincronizacion = estadoSincronizacion
        )
        crearPagoLocalRepositoryInterface.guardarPago(pagoEntity = savePago)
    }
}