package com.example.movapruebatecnica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movapruebatecnica.core.CREAR_PAGO_SCREEN
import com.example.movapruebatecnica.core.DETALLE_PAGO_SCREEN
import com.example.movapruebatecnica.core.LISTA_PAGO_SCREEN
import com.example.movapruebatecnica.core.LOGIN_SCREEN
import com.example.movapruebatecnica.core.MENU_SCREEN
import com.example.movapruebatecnica.core.theme.MovaPruebaTecnicaTheme
import com.example.movapruebatecnica.crearPagos.ui.view.CrearPagoScreen
import com.example.movapruebatecnica.detallePagos.ui.view.DetallePagoScreen
import com.example.movapruebatecnica.detallePagos.ui.viewModel.DetallePagoViewModel
import com.example.movapruebatecnica.listaPagos.ui.view.ListaPagoScreen
import com.example.movapruebatecnica.listaPagos.ui.viewModel.ListaPagoViewModel
import com.example.movapruebatecnica.login.ui.view.LoginScreen
import com.example.movapruebatecnica.login.ui.viewModel.LoginViewModel
import com.example.movapruebatecnica.menu.ui.view.MenuScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val loginViewModel: LoginViewModel by viewModels()
    val detallePagoViewModel: DetallePagoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovaPruebaTecnicaTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = LOGIN_SCREEN) {
                    composable(LOGIN_SCREEN){
                        LoginScreen(
                            loginViewModel = loginViewModel,
                            loginExitoso = {navController.navigate(MENU_SCREEN)}
                        )
                    }
                    composable(MENU_SCREEN) {
                        MenuScreen(
                            onRealizarPagoClick = {
                                navController.navigate(CREAR_PAGO_SCREEN)
                            },
                            onListarPagosClick = {
                                navController.navigate(LISTA_PAGO_SCREEN)
                            }
                        )
                    }
                    composable(LISTA_PAGO_SCREEN){
                        ListaPagoScreen(
                            onBack = { navController.popBackStack() },
                            navegarDetallePagoScreen = { referencia ->
                                navController.navigate("$DETALLE_PAGO_SCREEN/$referencia/false")
                            }
                        ) 
                    }
                    composable(CREAR_PAGO_SCREEN){
                        CrearPagoScreen(
                            onBack = {navController.popBackStack()},
                            navegarDetallePagoScreen = { referencia ->
                                navController.navigate("$DETALLE_PAGO_SCREEN/$referencia/true")
                            }
                        )
                    }

                    composable(
                        route = "$DETALLE_PAGO_SCREEN/{referencia}/{esNuevoPago}",
                        arguments = listOf(
                            navArgument("referencia") { type = NavType.StringType },
                            navArgument("esNuevoPago") { type = NavType.BoolType }
                        )
                    ) { backStackEntry ->
                        val referencia = backStackEntry.arguments?.getString("referencia").orEmpty()
                        val esNuevoPago = backStackEntry.arguments?.getBoolean("esNuevoPago") ?: false
                        DetallePagoScreen(
                            referencia = referencia,
                            esNuevoPago = esNuevoPago,
                            detallePagoViewModel = detallePagoViewModel,
                            onBack = {navController.popBackStack()},
                            navegarAMenu = {
                                navController.navigate(MENU_SCREEN) {
                                    popUpTo(MENU_SCREEN) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }

            }
        }
    }

}

