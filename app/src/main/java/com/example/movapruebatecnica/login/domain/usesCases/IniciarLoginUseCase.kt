package com.example.movapruebatecnica.login.domain.usesCases

import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.login.domain.model.CredencialesAutenticarLoginDomain
import com.example.movapruebatecnica.login.domain.repository.LoginRemoteRepositoryInterface
import javax.inject.Inject

class IniciarLoginUseCase @Inject constructor(
    private val loginRemoteRepositoryInterface: LoginRemoteRepositoryInterface
) {
    suspend operator fun invoke(credencialesLogin: CredencialesAutenticarLoginDomain): ApiResponseStatus<String> {
        if (credencialesLogin.usuario.isBlank() || credencialesLogin.password.isBlank()) {
            return ApiResponseStatus.Error("El correo y la contraseña no pueden estar vacíos.")
        }
        return loginRemoteRepositoryInterface.autenticarLogin(
            credencialesLogin = credencialesLogin
        )
    }
}