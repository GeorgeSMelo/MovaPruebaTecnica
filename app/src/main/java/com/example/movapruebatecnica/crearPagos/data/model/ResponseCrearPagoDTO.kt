package com.example.movapruebatecnica.crearPagos.data.model

import com.example.movapruebatecnica.crearPagos.domain.model.PagoCreadoDomain
import com.google.gson.annotations.SerializedName

data class ResponseCrearPagoDTO(
    @SerializedName("reference") val reference: String
)

fun ResponseCrearPagoDTO.fromResponseCrearDatoDTOToPagoCreadoDomain() : PagoCreadoDomain =
    PagoCreadoDomain(
        reference = reference
    )