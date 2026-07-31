package com.example.movapruebatecnica.crearPagos.domain.model

import com.example.movapruebatecnica.core.database.roomdb.entities.PaymentEntity
import com.example.movapruebatecnica.core.data.model.RequestRealizarPagoDTO
import com.example.movapruebatecnica.core.domain.model.EnumEstadoPago
import com.example.movapruebatecnica.core.domain.model.EnumEstadoSincronizacion
import com.example.movapruebatecnica.core.domain.model.EnumMetodoPago

data class RealizarPagoDomain(
    val monto: String,
    val externalReference: String,
    val method: EnumMetodoPago,
    val idempotencyKey: String,
    val createdAt: Long = System.currentTimeMillis(),
    val status: EnumEstadoPago
)

fun RealizarPagoDomain.fromRealizarPagoDomainToRequestRealizarPagoDTO() : RequestRealizarPagoDTO =
    RequestRealizarPagoDTO(
        monto = monto,
        externalReference = externalReference,
        metodoPago = method.name,
        idempotencyKey = idempotencyKey,
        createdAt = createdAt.toString(),
        status = EnumEstadoPago.obtenerRandomState().name
    )

fun RealizarPagoDomain.fromRealizarPagoDomianToPaymentEntity(
    estadoSincronizacion: EnumEstadoSincronizacion
) : PaymentEntity = PaymentEntity(
    reference = externalReference,
    amount = monto,
    paymentMethod = method.name,
    status = status.name,
    syncStatus = estadoSincronizacion.name,
    idempotencyKey = idempotencyKey,
    createdAt = createdAt.toString()
)