package com.example.movapruebatecnica.core.database.roomdb.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movapruebatecnica.core.database.roomdb.entities.PaymentEntity

@Dao
interface PaymentDAO {
    @Query("SELECT EXISTS(SELECT 1 FROM payments WHERE reference = :ref)")
    suspend fun existsByReference(ref: String): Boolean

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPayment(payment: PaymentEntity)

    @Query("SELECT * FROM payments WHERE reference = :referencia")
    suspend fun getPagoLocal(referencia: String) : PaymentEntity?

    @Query("SELECT * FROM payments")
    suspend fun obtenerTodosLosPagos() : List<PaymentEntity>

    @Query("Select * FROM payments WHERE syncStatus IN (:estados)")
    suspend fun obtenerPagosPorEstadosDeSincronizacion(estados: List<String>) : List<PaymentEntity>

    @Query("UPDATE payments SET syncStatus = :estadoSincronizacion WHERE reference IN (:listadoReferencias)")
    suspend fun actualizarEstadoSincronizacionDePagos(
        listadoReferencias: List<String>,
        estadoSincronizacion: String
    )

    @Query("UPDATE payments SET syncStatus = :estadoSincronizacion WHERE reference = :referencia")
    suspend fun actualizarEstadoSincronizacionDePago(
        referencia: String,
        estadoSincronizacion: String
    )
}