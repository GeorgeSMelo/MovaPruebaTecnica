package com.example.movapruebatecnica.listaPagos.domain.repository

import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.core.domain.model.EnumEstadoSincronizacion

interface ListaPagoLocalRepositoryInterface {
    suspend fun obtenerHistoricoDePagos() : List<DetallePagoDomain>
    suspend fun obtenerPagosPorEstadosDeSincronizacion(estados: List<String>) : List<DetallePagoDomain>

    suspend fun actualizarEstadoSincronizacionPagos(
        listadoReferencias : List<String>,
        nuevoEstadoSincronizacion : String
    )
}