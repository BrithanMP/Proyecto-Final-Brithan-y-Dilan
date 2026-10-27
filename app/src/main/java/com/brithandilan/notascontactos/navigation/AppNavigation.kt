package com.brithandilan.notascontactos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.brithandilan.notascontactos.ui.screens.HomeScreen
import com.brithandilan.notascontactos.ui.screens.LoginScreen
import com.brithandilan.notascontactos.ui.screens.RegisterScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brithandilan.notascontactos.viewmodel.AuthViewModel


@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val startDestination = if (authViewModel.haySesionActiva()) {
        "home"
    } else {
        "login"
    }




    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("login") {
            LoginScreen(
                mensajeError = authViewModel.mensajeError,
                cargando = authViewModel.cargando,
                onLoginClick = { correo, contrasena ->

                    authViewModel.iniciarSesion(
                        correo,
                        contrasena
                    ) {
                        navController.navigate("home") {
                            popUpTo("login") {
                                inclusive = true
                            }
                        }
                    }
                },

                onRegisterClick = {
                    navController.navigate("register") {
                        launchSingleTop = true
                    }
                }

            )
        }

        composable("register") {
            RegisterScreen(
                mensajeError = authViewModel.mensajeError,
                cargando = authViewModel.cargando,
                onRegisterClick = { _, correo, contrasena ->

                    authViewModel.registrarUsuario(
                        correo,
                        contrasena
                    ) {
                        navController.navigate("home") {
                            popUpTo("login") {
                                inclusive = true
                            }
                        }
                    }
                },

                onBackToLoginClick = {
                    navController.popBackStack()
                }
            )
        }



        composable("home") {
            HomeScreen(
                onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}
