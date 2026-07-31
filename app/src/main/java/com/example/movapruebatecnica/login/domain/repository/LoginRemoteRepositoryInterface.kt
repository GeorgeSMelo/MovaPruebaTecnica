package com.example.movapruebatecnica.login.domain.repository

import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.login.domain.model.CredencialesAutenticarLoginDomain

interface LoginRemoteRepositoryInterface {
    suspend fun autenticarLogin(credencialesLogin: CredencialesAutenticarLoginDomain) : ApiResponseStatus<String>
}