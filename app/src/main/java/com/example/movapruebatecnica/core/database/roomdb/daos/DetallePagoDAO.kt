package com.example.movapruebatecnica.core.database.roomdb.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movapruebatecnica.core.database.roomdb.entities.DetallePagoEntity

@Dao
interface DetallePagoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarPago(detallePagoEntity: DetallePagoEntity)

    @Query("SELECT * FROM DetallesPago")
    suspend fun obtenerPago(): DetallePagoEntity?

    @Query("DELETE FROM DetallesPago" )
    suspend fun eliminarPago()
}