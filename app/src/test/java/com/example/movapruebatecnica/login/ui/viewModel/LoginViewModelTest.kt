package com.example.movapruebatecnica.login.ui.viewModel.login.ui.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.login.domain.model.CredencialesAutenticarLoginDomain
import com.example.movapruebatecnica.login.domain.usesCases.IniciarLoginUseCase
import com.example.movapruebatecnica.login.ui.viewModel.LoginViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {
    @RelaxedMockK
    private lateinit var iniciarLoginUseCase: IniciarLoginUseCase

    private lateinit var loginViewModel: LoginViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        loginViewModel = LoginViewModel(
            iniciarLoginUseCase = iniciarLoginUseCase
        )
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfther() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Cuando iniciar login es exitoso, debe actualizar el estado`() = runTest {
        //Given
        val user = "mova@test.com"
        val password = "123456"

        val credenciales = CredencialesAutenticarLoginDomain(
            usuario = user,
            password = password
        )
        val respuestaExitosa = ApiResponseStatus.Success(
            data = "Login Exitoso"
        )
        coEvery {
            iniciarLoginUseCase(
                credencialesLogin = credenciales
            )
        } returns respuestaExitosa

        //When
        loginViewModel.iniciarLogin(
            user = user,
            password = password
        )

        //Then
        TestCase.assertEquals(
            respuestaExitosa,
            loginViewModel.stateIniciarSesion.value
        )

        TestCase.assertEquals(
            false,
            loginViewModel.isLoading.value
        )

        coVerify(exactly = 1) {
            iniciarLoginUseCase(
                credencialesLogin = credenciales
            )
        }
    }
    @Test
    fun `Cuando iniciar login es fallido, debe actualizarse el estado`() = runTest {
        // Given
        val user = "Hola@hotmail.com"
        val password = "3123321"

        val credenciales = CredencialesAutenticarLoginDomain(
            usuario = user,
            password = password
        )
        val respuestaFallida: ApiResponseStatus<String> = ApiResponseStatus.Error(
            message = "Fallo al autenticar las credenciales del usuario"
        )
        coEvery {
            iniciarLoginUseCase(
                credencialesLogin = credenciales
            )
        } returns respuestaFallida

        //When
        loginViewModel.iniciarLogin(
            user = user,
            password = password
        )
        TestCase.assertEquals(
            respuestaFallida,
            loginViewModel.stateIniciarSesion.value
        )
        TestCase.assertEquals(
            false,
            loginViewModel.isLoading.value
        )
        coVerify(exactly = 1) {
            iniciarLoginUseCase(
                credencialesLogin = credenciales
            )
        }
    }
}