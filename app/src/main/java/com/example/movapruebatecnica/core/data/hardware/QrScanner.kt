package com.example.movapruebatecnica.core.data.hardware

import android.content.Context
import com.example.movapruebatecnica.core.domain.hardware.QrScannerInterface
import com.example.movapruebatecnica.core.domain.model.QrResult
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class QrScanner @Inject constructor(
    @ApplicationContext private val context: Context
) : QrScannerInterface {

    private val scanner by lazy {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .enableAutoZoom()
            .build()
        GmsBarcodeScanning.getClient(context, options)
    }

    override suspend fun scan(): QrResult = suspendCancellableCoroutine { continuation ->
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val rawValue = barcode.rawValue
                if (!rawValue.isNullOrBlank()) {
                    continuation.resume(QrResult.Success(rawValue))
                } else {
                    continuation.resume(QrResult.Error("Contenido de QR vacío"))
                }
            }
            .addOnCanceledListener {
                continuation.resume(QrResult.Cancelled)
            }
            .addOnFailureListener { exception ->
                continuation.resume(
                    QrResult.Error(
                        exception.localizedMessage ?: "Error al escanear"
                    )
                )
            }
    }
}