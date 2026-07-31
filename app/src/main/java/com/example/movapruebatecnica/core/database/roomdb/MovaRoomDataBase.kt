package com.example.movapruebatecnica.core.database.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movapruebatecnica.core.database.roomdb.daos.DetallePagoDAO
import com.example.movapruebatecnica.core.database.roomdb.daos.PaymentDAO
import com.example.movapruebatecnica.core.database.roomdb.daos.UsuarioDAO
import com.example.movapruebatecnica.core.database.roomdb.entities.DetallePagoEntity
import com.example.movapruebatecnica.core.database.roomdb.entities.PaymentEntity
import com.example.movapruebatecnica.core.database.roomdb.entities.UsuarioEntity

@Database(
entities = [
    DetallePagoEntity::class,
    UsuarioEntity::class,
    PaymentEntity::class
],
    version =3,
    exportSchema = false
)

abstract class MovaRoomDataBase : RoomDatabase() {

    abstract fun myPaymentEntity(): PaymentDAO
    abstract fun myDetallePagoDAO(): DetallePagoDAO

    abstract fun myUsuarioDAO(): UsuarioDAO
}