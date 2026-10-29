
package com.brithandilan.notascontactos.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.brithandilan.notascontactos.model.Note
import com.brithandilan.notascontactos.ui.screens.HomeScreen
import com.brithandilan.notascontactos.ui.screens.LoginScreen
import com.brithandilan.notascontactos.ui.screens.NewNoteScreen
import com.brithandilan.notascontactos.ui.screens.NoteDetailScreen
import com.brithandilan.notascontactos.viewmodel.AuthViewModel
import com.brithandilan.notascontactos.viewmodel.NoteViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val authViewModel: AuthViewModel = viewModel()
    val noteViewModel: NoteViewModel = viewModel()

    // NOTA SELECCIONADA
    var notaSeleccionada by remember {
        mutableStateOf<Note?>(null)
    }

    // PANTALLA INICIAL
    val startDestination =
        if (authViewModel.haySesionActiva()) {
            "home"
        } else {
            "login"
        }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // LOGIN
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

        // HOME
        composable("home") {

            LaunchedEffect(Unit) {
                noteViewModel.cargarNotas()
            }

            HomeScreen(
                onLogoutClick = {
                    // Pendiente de conectar con AuthViewModel
                },

                onNewNoteClick = {
                    navController.navigate("newNote")
                },

                onNoteClick = { nota ->
                    notaSeleccionada = nota
                    navController.navigate("noteDetail")
                },

                notas = noteViewModel.notas
            )
        }

        // CREAR NUEVA NOTA
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

        // DETALLE DE NOTA
        composable("noteDetail") {

            val nota = notaSeleccionada

            if (nota != null) {

                NoteDetailScreen(
                    nota = nota,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
