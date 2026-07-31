package com.example.movapruebatecnica.login.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.login.domain.model.CredencialesAutenticarLoginDomain
import com.example.movapruebatecnica.login.domain.usesCases.IniciarLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val iniciarLoginUseCase: IniciarLoginUseCase
): ViewModel(){
    private val _stateIniciarSesion = MutableStateFlow<ApiResponseStatus<String>?>(null)
    val stateIniciarSesion: StateFlow<ApiResponseStatus<String>?> = _stateIniciarSesion.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading.asStateFlow()

    fun iniciarLogin(
        user: String,
        password: String
    ) =viewModelScope.launch {
        _isLoading.value = true
        val credencialesLogin = CredencialesAutenticarLoginDomain(
            usuario = user,
            password = password
        )
        val respuestaApi = iniciarLoginUseCase(
            credencialesLogin = credencialesLogin
        )
        if (respuestaApi is ApiResponseStatus.Success){
            respuestaApi.data
        }
        _stateIniciarSesion.value = respuestaApi
        _isLoading.value = false
    }

}