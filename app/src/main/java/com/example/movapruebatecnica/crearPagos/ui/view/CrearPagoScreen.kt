package com.example.movapruebatecnica.crearPagos.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movapruebatecnica.R
import com.example.movapruebatecnica.core.domain.model.EnumMetodoPago
import com.example.movapruebatecnica.core.domain.model.NfcPaymentResult
import com.example.movapruebatecnica.core.internet.ApiResponseStatus
import com.example.movapruebatecnica.core.ui.LoaderBar
import com.example.movapruebatecnica.crearPagos.ui.viewModel.CrearPagoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearPagoScreen(
    crearPagoViewModel: CrearPagoViewModel = hiltViewModel(),
    onBack: () -> Unit,
    navegarDetallePagoScreen: (String) -> Unit
) {
    val uiState by crearPagoViewModel.uiState.collectAsState()
    val isLoading by crearPagoViewModel.isLoading.collectAsState()
    val stateRealizarPago by crearPagoViewModel.stateRealizarPago.collectAsState()
    val stateNfcPayment by crearPagoViewModel.stateNfcPayment.collectAsState()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    if (isLoading) {
        LoaderBar()
    }

    LaunchedEffect(uiState.mostrarErrorQrDialog) {
        if (uiState.mostrarErrorQrDialog) {
            snackbarHostState.showSnackbar(
                message = "Falló la lectura del QR. Por favor, vuelva a intentarlo.",
                duration = SnackbarDuration.Short
            )
            crearPagoViewModel.ocultarAlertaErrorQr()
        }
    }

    LaunchedEffect(uiState.isValid, uiState.montoMensajeError) {
        if (!uiState.isValid && !uiState.montoMensajeError.isNullOrBlank()) {
            snackbarHostState.showSnackbar(
                message = uiState.montoMensajeError ?: "Formulario no válido",
                duration = SnackbarDuration.Short
            )
        }
    }

    LaunchedEffect(stateNfcPayment) {
        if (stateNfcPayment is NfcPaymentResult.Error) {
            snackbarHostState.showSnackbar(
                message = (stateNfcPayment as NfcPaymentResult.Error).message,
                duration = SnackbarDuration.Short
            )
        }
    }

    when (stateRealizarPago) {
        is ApiResponseStatus.Error, is ApiResponseStatus.Success -> {
            navegarDetallePagoScreen(
                uiState.reference
            )
        }

        else -> Unit
    }

    LaunchedEffect(Unit) {
        crearPagoViewModel.getReferenciaPago()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.crearPago))
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                FloatingActionButton(
                    onClick = {
                        crearPagoViewModel.procesarPagoNFC()
                    },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(
                        imageVector = Icons.Default.Nfc,
                        contentDescription = "Simular Pago NFC"
                    )
                }

                FloatingActionButton(
                    onClick = { crearPagoViewModel.iniciarEscaneoQr() },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "Escanear QR"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Inserte monto de transferencia",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = uiState.monto,
                onValueChange = {
                    crearPagoViewModel.actualizarMonto(it)
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Valor Monetario")
                },
                placeholder = {
                    Text("123456789")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Referencia",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                OutlinedTextField(
                    value = uiState.reference,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Código de Referencia") },
                    singleLine = true
                )
            }

            Text(
                text = "Método de Pago",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EnumMetodoPago.entries.forEach { method ->
                    FilterChip(
                        selected = uiState.selectedMethod == method,
                        onClick = { crearPagoViewModel.onMethodSelected(method) },
                        label = { Text(method.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (uiState.isValid) {
                        crearPagoViewModel.realizarPago()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(20),
                enabled = !isLoading
            ) {
                Text(
                    text = "Realizar Pago",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}
