package com.example.movapruebatecnica.listaPagos.data.repository

import com.example.movapruebatecnica.core.database.roomdb.daos.PaymentDAO
import com.example.movapruebatecnica.core.database.roomdb.entities.fromListPaymentEntityToListDetallePagoDomain
import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.listaPagos.domain.repository.ListaPagoLocalRepositoryInterface
import javax.inject.Inject

class ListaPagoLocalRepository @Inject constructor(
    private val paymentDAO: PaymentDAO
) : ListaPagoLocalRepositoryInterface {

    override suspend fun obtenerHistoricoDePagos() : List<DetallePagoDomain> {
        val pagosLocales = paymentDAO.obtenerTodosLosPagos()
        return pagosLocales.fromListPaymentEntityToListDetallePagoDomain()
    }

    override suspend fun obtenerPagosPorEstadosDeSincronizacion(estados: List<String>): List<DetallePagoDomain> {
        val pagosPendientesPorSincronizar = paymentDAO.obtenerPagosPorEstadosDeSincronizacion(estados)
        return pagosPendientesPorSincronizar.fromListPaymentEntityToListDetallePagoDomain()
    }

    override suspend fun actualizarEstadoSincronizacionPagos(
        listadoReferencias: List<String>,
        nuevoEstadoSincronizacion: String
    ) {
        paymentDAO.actualizarEstadoSincronizacionDePagos(
            listadoReferencias = listadoReferencias,
            estadoSincronizacion = nuevoEstadoSincronizacion
        )
    }
}