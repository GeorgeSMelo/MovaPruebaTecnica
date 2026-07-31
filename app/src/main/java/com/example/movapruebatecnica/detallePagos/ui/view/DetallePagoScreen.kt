package com.example.movapruebatecnica.detallePagos.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movapruebatecnica.R
import com.example.movapruebatecnica.core.toFormattedDate
import com.example.movapruebatecnica.core.ui.LoaderBar
import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.core.domain.model.EnumEstadoSincronizacion
import com.example.movapruebatecnica.core.domain.model.NfcPaymentResult
import com.example.movapruebatecnica.core.domain.model.PrintResult
import com.example.movapruebatecnica.detallePagos.ui.viewModel.DetallePagoViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePagoScreen(
    detallePagoViewModel: DetallePagoViewModel,
    onBack: () -> Unit,
    navegarAMenu: () -> Unit,
    referencia: String,
    esNuevoPago: Boolean
) {
    LaunchedEffect(referencia) {
        detallePagoViewModel.obtenerDatosDeDetallePago(referencia)
    }

    val isLoading by detallePagoViewModel.isLoading.collectAsState()
    val dataDetallePago by detallePagoViewModel.detalleDePago.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val stateImpresion by detallePagoViewModel.stateImpresion.collectAsState()

    if (isLoading) {
        LoaderBar()
    }

    LaunchedEffect(stateImpresion) {
        when(stateImpresion) {
            is PrintResult.Success -> {
                snackbarHostState.showSnackbar(
                    message = "Impresión exitosa!!",
                    duration = SnackbarDuration.Short
                )
            }
            is PrintResult.Error -> {
                snackbarHostState.showSnackbar(
                    message = (stateImpresion as PrintResult.Error).message,
                    duration = SnackbarDuration.Short
                )
            }
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.detallePago))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (esNuevoPago) {
                                navegarAMenu()
                            } else {
                                onBack()
                            }
                        }
                    ) {
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            dataDetallePago?.let { detalle ->
                CardInfoDetalle(detalle = detalle)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (dataDetallePago?.syncStatus != EnumEstadoSincronizacion.SYNCED) {
                    Button(
                        onClick = {
                            detallePagoViewModel.sincronizarPago()
                        },
                        modifier = Modifier.weight(1f).height(65.dp),
                        shape = RoundedCornerShape(20),
                        enabled = !isLoading
                    ) {
                        Text(
                            text = stringResource(R.string.sincronizar),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Button(
                    onClick = { detallePagoViewModel.imprimirDetallePago() },
                    modifier = Modifier.weight(1f).height(65.dp),
                    shape = RoundedCornerShape(20)
                ) {
                    Text(
                        text = stringResource(R.string.imprimir),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun CardInfoDetalle(detalle: DetallePagoDomain) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Resumen de la Transacción",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            HorizontalDivider()

            ItemDetalleRow(label = "Referencia:", value = detalle.reference)
            ItemDetalleRow(label = "Monto:", value = "$ ${detalle.amount}")
            ItemDetalleRow(label = "Método de Pago:", value = detalle.paymentMethod.name)
            ItemDetalleRow(label = "Estado:", value = detalle.status.name)
            ItemDetalleRow(label = "Sincronización:", value = detalle.syncStatus.name)
            ItemDetalleRow(label = "Fecha:", value = detalle.createdAt.toFormattedDate())
            ItemDetalleRow(label = "Idempotency Key:", value = detalle.idempotencyKey)
        }
    }
}

@Composable
fun ItemDetalleRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
    }
}