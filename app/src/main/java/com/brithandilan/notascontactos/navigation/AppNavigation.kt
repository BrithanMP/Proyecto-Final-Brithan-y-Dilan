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
import com.brithandilan.notascontactos.ui.screens.NewNoteScreen
import com.brithandilan.notascontactos.viewmodel.NoteViewModel
import androidx.compose.runtime.LaunchedEffect

@Composable
fun AppNavigation() {


    val noteViewModel: NoteViewModel = viewModel()
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


        composable("newNote") {

            NewNoteScreen(
                onSaveClick = { titulo, contenido ->

                    noteViewModel.guardarNota(
                        titulo = titulo,
                        contenido = contenido,
                        onSuccess = {
                            navController.popBackStack()
                        }
                    )
                },

                onCancelClick = {
                    noteViewModel.limpiarError()
                    navController.popBackStack()
                },

                mensajeError = noteViewModel.mensajeError,
                cargando = noteViewModel.cargando
            )
        }






        composable("home") {

            LaunchedEffect(Unit) {
                noteViewModel.cargarNotas()
            }

            HomeScreen(
                onLogoutClick = {
                    // CONSERVA AQUÍ TU CÓDIGO ACTUAL
                    // PARA CERRAR SESIÓN
                },

                onNewNoteClick = {
                    navController.navigate("newNote")
                },

                notas = noteViewModel.notas
            )
        }


        composable("newNote") {
            NewNoteScreen(
                onSaveClick = { _, _ ->
                    // Guardado real pendiente para el periodo 6.
                    navController.popBackStack()
                },
                onCancelClick = {
                    navController.popBackStack()
                }
            )
        }


    }

}

