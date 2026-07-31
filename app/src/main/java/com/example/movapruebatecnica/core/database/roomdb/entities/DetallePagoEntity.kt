package com.example.movapruebatecnica.core.database.roomdb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("DetallesPago")
data class DetallePagoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val referencia : String,
    val valor: Int,
    val estado: String,
    val metodoDePago: String,
    val fechaCreacion: String,
    val estadoSincronizacion : String,
    val llavedeidempotencia: String
)