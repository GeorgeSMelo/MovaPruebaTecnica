package com.example.movapruebatecnica.listaPagos.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movapruebatecnica.R
import com.example.movapruebatecnica.core.domain.model.EnumEstadoSincronizacion
import com.example.movapruebatecnica.core.toFormattedDate
import com.example.movapruebatecnica.core.domain.model.DetallePagoDomain
import com.example.movapruebatecnica.core.ui.LoaderBar
import com.example.movapruebatecnica.listaPagos.ui.viewModel.ListaPagoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaPagoScreen(
    listaPagoViewModel: ListaPagoViewModel = hiltViewModel(),
    onBack: () -> Unit,
    navegarDetallePagoScreen: (String) -> Unit
) {
    val isLoading by listaPagoViewModel.isLoading.collectAsState()
    val historicoPagos by listaPagoViewModel.historicoPagos.collectAsState()
    val estadosSeleccionados by listaPagoViewModel.estadosSeleccionados.collectAsState()

    LaunchedEffect(Unit) {
        listaPagoViewModel.obtenerHistoricoPagos()
    }

    if (isLoading) {
        LoaderBar()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.listadepagos))
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
            ExtendedFloatingActionButton(
                onClick = { listaPagoViewModel.sincronizarPagosLocales() },
                icon = { Icon(Icons.Default.Refresh, contentDescription = null) },
                text = { Text("Sincronizar pagos", fontWeight = FontWeight.Bold) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            FiltroEstadosDeSincronizacion(
                metodosSeleccionados = estadosSeleccionados,
                onEstadoSeleccionado = { estado ->
                    listaPagoViewModel.actualizarFiltrosEstadoSincronizacion(estado)
                }
            )

            if (historicoPagos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay pagos registrados",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                ListaPagos(
                    pagos = historicoPagos,
                    onItemClick = { referencia ->
                        navegarDetallePagoScreen(referencia)
                    }
                )
            }
        }
    }
}

@Composable
fun ListaPagos(
    pagos: List<DetallePagoDomain>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pagos, key = { it.reference }) { pago ->
            ItemPagoCard(pago = pago, onClick = { onItemClick(pago.reference) })
        }
    }
}

@Composable
fun ItemPagoCard(
    pago: DetallePagoDomain,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pago.reference,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                BadgeEstadoPago(status = pago.status.name)
            }

            Text(
                text = "$ ${pago.amount}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Método: ${pago.paymentMethod.name}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = pago.createdAt.toFormattedDate(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                BadgeSyncStatus(syncStatus = pago.syncStatus)
            }
        }
    }
}

@Composable
fun BadgeEstadoPago(status: String) {
    val (color, texto) = when (status.uppercase()) {
        "APPROVED" -> Color(0xFF2E7D32) to "APPROVED"
        "PENDING" -> Color(0xFFED6C02) to "PENDING"
        "REJECTED" -> Color(0xFFD32F2F) to "REJECTED"
        "CANCELLED" -> Color(0xFF000000) to "CANCELLED"
        else -> Color.Gray to status
    }

    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = texto,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BadgeSyncStatus(syncStatus: EnumEstadoSincronizacion) {
    val (color, texto) = when (syncStatus) {
        EnumEstadoSincronizacion.SYNCED -> Color(0xFF2E7D32) to "SYNCED"
        EnumEstadoSincronizacion.PENDING_SYNC -> Color(0xFFED6C02) to "PENDING_SYNC"
        EnumEstadoSincronizacion.SYNC_ERROR -> Color(0xFFD32F2F) to "SYNC_ERROR"
    }

    Text(
        text = "• $texto",
        color = color,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun FiltroEstadosDeSincronizacion(
    metodosSeleccionados: Set<EnumEstadoSincronizacion>,
    onEstadoSeleccionado: (EnumEstadoSincronizacion) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(EnumEstadoSincronizacion.entries.toTypedArray()) { estado ->
            val isSelected = metodosSeleccionados.contains(estado)
            FilterChip(
                selected = isSelected,
                onClick = { onEstadoSeleccionado(estado) },
                label = { Text(estado.name) },
                leadingIcon = if (isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null
                        )
                    }
                } else null
            )
        }
    }
}