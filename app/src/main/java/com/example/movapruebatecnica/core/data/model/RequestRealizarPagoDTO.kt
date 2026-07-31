package com.example.movapruebatecnica.core.data.model

import com.google.gson.annotations.SerializedName

data class RequestRealizarPagoDTO(
    @SerializedName("monto") val monto: String,
    @SerializedName("reference") val externalReference: String,
    @SerializedName("method") val metodoPago: String,
    @SerializedName("idempotencyKey") val idempotencyKey: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("status") val status: String
)