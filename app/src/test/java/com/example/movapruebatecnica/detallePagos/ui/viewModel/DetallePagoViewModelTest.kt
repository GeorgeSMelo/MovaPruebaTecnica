package com.example.movapruebatecnica.detallePagos.ui.viewModel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movapruebatecnica.core.domain.useCases.ReceiptPrinterUseCase
import com.example.movapruebatecnica.detallePagos.domain.usesCases.ObtenerDetalleDePagoLocalUseCase
import com.example.movapruebatecnica.detallePagos.domain.usesCases.ObtenerDetalleDePagoRemotoUseCase
import com.example.movapruebatecnica.detallePagos.domain.usesCases.SincronizarPagoUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetallePagoViewModelTest {
    @RelaxedMockK
    private lateinit var obtenerDetalleDePagoLocalUseCase: ObtenerDetalleDePagoLocalUseCase
    private lateinit var obtenerDetalleDePagoRemotoUseCase: ObtenerDetalleDePagoRemotoUseCase
    private lateinit var sincronizarPagoUseCase: SincronizarPagoUseCase
    private lateinit var receiptPrinterUseCase: ReceiptPrinterUseCase

    private lateinit var detallePagoViewModel: DetallePagoViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        detallePagoViewModel = DetallePagoViewModel(
            obtenerDetalleDePagoLocalUseCase = obtenerDetalleDePagoLocalUseCase,
            obtenerDetalleDePagoRemotoUseCase = obtenerDetalleDePagoRemotoUseCase,
            sincronizarPagoUseCase = sincronizarPagoUseCase,
            receiptPrinterUseCase = receiptPrinterUseCase
        )
        Dispatchers.setMain(
            Dispatchers.Unconfined
        )

    }
    @Test
    fun `Cuando la referencia existe Localmente no necesesita buscarla de forma Remota`() = runTest{

    }

}
