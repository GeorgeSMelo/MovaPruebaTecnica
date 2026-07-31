package com.example.movapruebatecnica.listaPagos.data.model

import com.example.movapruebatecnica.core.domain.model.EnumMetodoPago
import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.core.domain.model.EnumEstadoPago
import com.example.movapruebatecnica.core.domain.model.EnumEstadoSincronizacion
import com.google.gson.annotations.SerializedName

data class ResponseDetalleDePagoDTO (
    @SerializedName("reference") val referencia : String,
    @SerializedName("amount") val amount : String,
    @SerializedName("payment_method") val paymentMethod : String,
    @SerializedName("status") val status: String,
    @SerializedName("created_at") val createdAt : String,
    @SerializedName("idempotencyKey") val idempotencyKey: String
)

fun ResponseDetalleDePagoDTO.fromResponseDetalleDePagoDTOToDetallePagoDomain(): DetallePagoDomain {
    return  DetallePagoDomain(
        reference = referencia,
        amount = amount,
        paymentMethod = EnumMetodoPago.valueOf(paymentMethod),
        status = EnumEstadoPago.valueOf(status),
        createdAt = createdAt.toLong(),
        syncStatus = EnumEstadoSincronizacion.SYNCED,
        idempotencyKey = idempotencyKey
    )
}
