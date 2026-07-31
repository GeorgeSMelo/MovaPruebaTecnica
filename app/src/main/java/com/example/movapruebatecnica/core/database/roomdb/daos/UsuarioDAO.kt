package com.example.movapruebatecnica.core.database.roomdb.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movapruebatecnica.core.database.roomdb.entities.UsuarioEntity


@Dao
interface UsuarioDAO {

    @Query("SELECT * FROM usuarios")
    suspend fun loginUsuario(): UsuarioEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarUsuario(usuarioEntity: UsuarioEntity)

    @Query("DELETE FROM usuarios")
    suspend fun limpiarLoginUsuario()
}