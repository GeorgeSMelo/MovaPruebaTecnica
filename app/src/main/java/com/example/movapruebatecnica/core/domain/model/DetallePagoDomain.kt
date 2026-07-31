package com.example.movapruebatecnica.core.domain.model

import com.example.movapruebatecnica.core.data.model.RequestRealizarPagoDTO

data class DetallePagoDomain(
    val reference : String,
    val amount : String,
    val paymentMethod : EnumMetodoPago,
    val status: EnumEstadoPago,
    val syncStatus: EnumEstadoSincronizacion,
    val createdAt : Long,
    val idempotencyKey: String
)

fun List<DetallePagoDomain>.fromListDetallePagoDomainToListRequestRealizarPagoDTO(): List<RequestRealizarPagoDTO> {
    return this.map { it.fromDetallePagoDomainToRequestRealizarPagoDTO() }
}

fun DetallePagoDomain.fromDetallePagoDomainToRequestRealizarPagoDTO() : RequestRealizarPagoDTO = RequestRealizarPagoDTO(
    monto = amount,
    externalReference = reference,
    metodoPago = paymentMethod.name,
    idempotencyKey = idempotencyKey,
    createdAt = createdAt.toString(),
    status = status.name
)

fun DetallePagoDomain?.fromDetallePagoDomianToReceiptDomain() : ReceiptDomain = ReceiptDomain(
    monto = this?.amount ?: "0"
)