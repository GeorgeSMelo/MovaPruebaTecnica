package com.example.movapruebatecnica.core.di

import android.content.Context
import androidx.room.Room
import com.example.movapruebatecnica.core.ROOM_DATABASE_NAME
import com.example.movapruebatecnica.core.data.hardware.NfcPaymentReader
import com.example.movapruebatecnica.core.database.roomdb.MovaRoomDataBase
import com.example.movapruebatecnica.core.database.roomdb.daos.DetallePagoDAO
import com.example.movapruebatecnica.core.database.roomdb.daos.PaymentDAO
import com.example.movapruebatecnica.core.database.roomdb.daos.UsuarioDAO
import com.example.movapruebatecnica.core.internet.MovApi
import com.example.movapruebatecnica.crearPagos.data.repository.CrearPagoLocalRepository
import com.example.movapruebatecnica.crearPagos.data.repository.CrearPagoRemoteRepository
import com.example.movapruebatecnica.core.data.hardware.QrScanner
import com.example.movapruebatecnica.core.data.hardware.ReceiptPrinter
import com.example.movapruebatecnica.core.domain.hardware.NfcPaymentReaderInterface
import com.example.movapruebatecnica.crearPagos.domain.repository.CrearPagoLocalRepositoryInterface
import com.example.movapruebatecnica.crearPagos.domain.repository.CrearPagoRemoteRepositoryInterface
import com.example.movapruebatecnica.core.domain.hardware.QrScannerInterface
import com.example.movapruebatecnica.core.domain.hardware.ReceiptPrinterInterface
import com.example.movapruebatecnica.detallePagos.data.repository.DetallePagoLocalRespository
import com.example.movapruebatecnica.detallePagos.data.repository.DetallePagoRemoteRepository
import com.example.movapruebatecnica.detallePagos.domain.repository.DetallePagoLocalRepositoryInterface
import com.example.movapruebatecnica.detallePagos.domain.repository.DetallePagoRemoteRepositoryInterface
import com.example.movapruebatecnica.listaPagos.data.repository.ListaPagoLocalRepository
import com.example.movapruebatecnica.listaPagos.data.repository.ListaPagoRemoteRepository
import com.example.movapruebatecnica.listaPagos.domain.repository.ListaPagoLocalRepositoryInterface
import com.example.movapruebatecnica.listaPagos.domain.repository.ListaPagoRemoteRepositoryInterface
import com.example.movapruebatecnica.login.data.repository.LoginRemoteRepository
import com.example.movapruebatecnica.login.domain.repository.LoginRemoteRepositoryInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object Injector {

    //Retrofit
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://movapi-fd91e-default-rtdb.firebaseio.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideMovApi(retrofit: Retrofit): MovApi {
        return retrofit.create(MovApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginRemoteRepositoryInterface(
        loginRemoteRepository: LoginRemoteRepository
    ): LoginRemoteRepositoryInterface = loginRemoteRepository

    @Singleton
    @Provides
    fun provideObtenerPagoRemoteRepositoryInterface(
        obtenerPagoRepository: DetallePagoRemoteRepository
    ): DetallePagoRemoteRepositoryInterface = obtenerPagoRepository

    @Singleton
    @Provides
    fun provideListaPagoRemoteRepositoryInterface(
        listaPagoRemoteRepository: ListaPagoRemoteRepository
    ): ListaPagoRemoteRepositoryInterface = listaPagoRemoteRepository

    @Singleton
    @Provides
    fun provideCrearPagoRemoteRepositoryInterface(
        crearPagoRepository: CrearPagoRemoteRepository
    ): CrearPagoRemoteRepositoryInterface = crearPagoRepository

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    //ROOM
    @Singleton
    @Provides
    fun provideMovaRoomDataBase(@ApplicationContext context: Context): MovaRoomDataBase {
        return Room.databaseBuilder(
            context = context,
            klass = MovaRoomDataBase::class.java,
            name = ROOM_DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideCrearPagoLocalRepositoryInterface(
        crearPagoRepository: CrearPagoLocalRepository
    ): CrearPagoLocalRepositoryInterface = crearPagoRepository

    @Singleton
    @Provides
    fun provideDetallePagoLocalRepositoryInterface(
        detallePagoLocalRepository: DetallePagoLocalRespository
    ): DetallePagoLocalRepositoryInterface = detallePagoLocalRepository

    @Singleton
    @Provides
    fun provideListaPagoLocalRepositoryInterface(
        listaPagoLocalRepository: ListaPagoLocalRepository
    ): ListaPagoLocalRepositoryInterface = listaPagoLocalRepository

    @Singleton
    @Provides
    fun providePaymentDao(dataBase: MovaRoomDataBase): PaymentDAO {
        return dataBase.myPaymentEntity()
    }

    @Singleton
    @Provides
    fun provideDetallePagoDAO(dataBase: MovaRoomDataBase): DetallePagoDAO {
        return dataBase.myDetallePagoDAO()
    }

    @Singleton
    @Provides
    fun provideUsuarioDAO(dataBase: MovaRoomDataBase): UsuarioDAO {
        return dataBase.myUsuarioDAO()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideQrScannerInterface(
        scannerImpl: QrScanner
    ): QrScannerInterface = scannerImpl

    @Singleton
    @Provides
    fun provideNfcPaymentReaderInterface(
        nfcPaymentReader: NfcPaymentReader
    ): NfcPaymentReaderInterface = nfcPaymentReader

    @Singleton
    @Provides
    fun provideReceiptPrinter(
        receiptPrinter: ReceiptPrinter
    ): ReceiptPrinterInterface = receiptPrinter
}