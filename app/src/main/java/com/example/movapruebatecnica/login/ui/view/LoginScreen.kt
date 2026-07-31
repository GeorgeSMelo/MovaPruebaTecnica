package com.example.movapruebatecnica.login.ui.view


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movapruebatecnica.core.ui.LoaderBar
import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.login.ui.viewModel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    loginExitoso: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val stateIniciarSesion by loginViewModel.stateIniciarSesion.collectAsStateWithLifecycle()
    val isLoading by loginViewModel.isLoading.collectAsStateWithLifecycle()
    val scopeSnackbar = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    fun showSnackbar(mensaje: String) {
        scopeSnackbar.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            snackbarHostState.showSnackbar(
                message = mensaje,
                duration = SnackbarDuration.Short
            )
        }
    }

    if (isLoading) {
        LoaderBar()
    }

    when (stateIniciarSesion) {
        is ApiResponseStatus.Error -> {
            showSnackbar(
                mensaje = (stateIniciarSesion as ApiResponseStatus.Error).message
            )
        }
        is ApiResponseStatus.Success -> {
            loginExitoso()
        }
        else -> Unit
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { escribirEmail ->
                    email = escribirEmail
                },
                label = {
                    Text(text = "Correo electronico")
                },
                placeholder = {
                    Text("User@gmail.com")
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { escribirPassword ->
                    password = escribirPassword
                },
                label = {
                    Text(text = "Password")
                },
                placeholder = {
                    Text("*******")
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    loginViewModel.iniciarLogin(
                        user = email,
                        password = password
                    )
                          },
                modifier = Modifier
                    .height(50.dp),
                shape = RoundedCornerShape(20),
                enabled = !isLoading
            ) {
                Text(
                    text = "Ingresar",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold

                )
            }

        }
    }
}
