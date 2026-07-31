package com.example.movapruebatecnica.core.internet

import com.example.movapruebatecnica.core.data.model.RequestRealizarPagoDTO
import com.example.movapruebatecnica.crearPagos.data.model.ResponseCrearPagoDTO
import com.example.movapruebatecnica.detallePagos.data.model.ResponseObtenerDetallePagoDTO
import com.example.movapruebatecnica.listaPagos.data.model.ResponseDetalleDePagoDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MovApi {

    @GET("/payments")
    suspend fun getPayments() : Response<List<ResponseObtenerDetallePagoDTO>>

    @POST("/payments")
    suspend fun processPayment(
        @Body body: RequestRealizarPagoDTO
    ) : Response<ResponseCrearPagoDTO>

    @GET("/payments/{referencia}")
    suspend fun getDetalleDePago(
        @Query("referencia") referencia: String
    ) : Response<ResponseDetalleDePagoDTO>

    @POST("/sincronizarPagos")
    suspend fun sincronizarPagosLocales(
        @Body body: List<RequestRealizarPagoDTO>
    ) : Response<Unit>
}