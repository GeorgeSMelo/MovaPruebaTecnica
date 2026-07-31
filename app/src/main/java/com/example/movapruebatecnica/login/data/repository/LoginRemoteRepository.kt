package com.example.movapruebatecnica.login.data.repository

import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.login.domain.model.CredencialesAutenticarLoginDomain
import com.example.movapruebatecnica.login.domain.repository.LoginRemoteRepositoryInterface
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRemoteRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : LoginRemoteRepositoryInterface {
    override suspend fun autenticarLogin(
        credencialesLogin: CredencialesAutenticarLoginDomain
    ): ApiResponseStatus<String> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(
                credencialesLogin.usuario,
                credencialesLogin.password
            ).await()
            val tokenResult = authResult.user?.getIdToken(false)?.await()
            val token = tokenResult?.token

            if (token != null) {
                ApiResponseStatus.Success(token)
            } else {
                ApiResponseStatus.Error("No se pudo obtener el token de autenticación.")
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            ApiResponseStatus.Error("Credenciales inválidas. Verifica tu correo o contraseña")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            ApiResponseStatus.Error("Credenciales inválidas. Verifica tu correo o contraseña")
        } catch (e: FirebaseNetworkException) {
            ApiResponseStatus.Error("No tiene internet, intente de nuevo.")
        } catch (e: Exception) {
            ApiResponseStatus.Error(
                e.localizedMessage ?: "Ocurrió un error inesperado al iniciar sesión"
            )
        }
    }

}