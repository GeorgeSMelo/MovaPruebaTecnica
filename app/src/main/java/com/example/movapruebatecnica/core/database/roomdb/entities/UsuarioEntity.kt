package com.example.movapruebatecnica.core.database.roomdb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuario: String,
    val password: String

)
