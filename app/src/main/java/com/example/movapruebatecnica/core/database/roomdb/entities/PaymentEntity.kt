package com.example.movapruebatecnica.core.database.roomdb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movapruebatecnica.core.domain.model.EnumMetodoPago
import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.core.domain.model.EnumEstadoPago
import com.example.movapruebatecnica.core.domain.model.EnumEstadoSincronizacion

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey
    val reference: String,
    val amount: String,
    val paymentMethod: String,
    val status: String,
    val syncStatus: String,
    val idempotencyKey: String,
    val createdAt: String
)

fun PaymentEntity.fromPaymentEntityToDetallePagoDomain() : DetallePagoDomain = DetallePagoDomain(
    reference = reference,
    amount = amount,
    paymentMethod = EnumMetodoPago.valueOf(paymentMethod),
    status = EnumEstadoPago.valueOf(status),
    syncStatus = EnumEstadoSincronizacion.valueOf(syncStatus),
    createdAt = createdAt.toLong(),
    idempotencyKey = idempotencyKey
)

fun List<PaymentEntity>.fromListPaymentEntityToListDetallePagoDomain() : List<DetallePagoDomain> {
    val listadoDetalles = mutableListOf<DetallePagoDomain>()

    this.forEach { pagoEntity ->
        val detallePagoDomain = pagoEntity.fromPaymentEntityToDetallePagoDomain()
        listadoDetalles.add(detallePagoDomain)
    }

    return listadoDetalles.toList()
}