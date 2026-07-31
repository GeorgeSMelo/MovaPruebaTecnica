package com.example.movapruebatecnica.detallePagos.data.model

import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.google.gson.annotations.SerializedName

data class ResponseObtenerDetallePagoDTO(
    @SerializedName("id") val id : String,
    @SerializedName("external_reference") val externalReference : String,
    @SerializedName("amount") val amount : Int,
    @SerializedName("currency") val currency: String,
    @SerializedName("payment_method") val paymentMethod : String,
    @SerializedName("status") val status: String,
    @SerializedName("created_at") val createdAt : String,
)

