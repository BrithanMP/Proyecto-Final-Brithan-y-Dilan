package com.brithandilan.notascontactos.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.brithandilan.notascontactos.model.Note
import com.brithandilan.notascontactos.ui.screens.ContactsScreen
import com.brithandilan.notascontactos.ui.screens.HomeScreen
import com.brithandilan.notascontactos.ui.screens.LoginScreen
import com.brithandilan.notascontactos.ui.screens.NewContactScreen
import com.brithandilan.notascontactos.ui.screens.NewNoteScreen
import com.brithandilan.notascontactos.ui.screens.NoteDetailScreen
import com.brithandilan.notascontactos.ui.screens.RegisterScreen
import com.brithandilan.notascontactos.viewmodel.AuthViewModel
import com.brithandilan.notascontactos.viewmodel.ContactViewModel
import com.brithandilan.notascontactos.viewmodel.NoteViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val authViewModel: AuthViewModel = viewModel()
    val noteViewModel: NoteViewModel = viewModel()
    val contactViewModel: ContactViewModel = viewModel()

    var notaSeleccionada by remember {
        mutableStateOf<Note?>(null)
    }

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
                            launchSingleTop = true
                        }
                    }
                },

                onRegisterClick = {
                    authViewModel.limpiarError()

                    navController.navigate("register") {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("register") {

            RegisterScreen(
                onRegisterClick = { nombre, correo, contrasena ->

                    authViewModel.registrarUsuario(
                        nombre = nombre,
                        correo = correo,
                        contrasena = contrasena
                    ) {
                        navController.navigate("home") {
                            popUpTo("login") {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                },

                onBackToLoginClick = {
                    authViewModel.limpiarError()
                    navController.popBackStack()
                },

                mensajeError = authViewModel.mensajeError,
                cargando = authViewModel.cargando
            )
        }

        composable("home") {

            LaunchedEffect(Unit) {
                noteViewModel.cargarNotas()
            }

            HomeScreen(
                onLogoutClick = {
                    authViewModel.cerrarSesion()
                    noteViewModel.limpiarError()
                    contactViewModel.limpiarError()
                    notaSeleccionada = null

                    navController.navigate("login") {
                        popUpTo("home") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },

                onNewNoteClick = {
                    noteViewModel.limpiarError()
                    navController.navigate("newNote")
                },

                onNoteClick = { nota ->
                    noteViewModel.limpiarError()
                    notaSeleccionada = nota
                    navController.navigate("noteDetail")
                },

                onContactsClick = {
                    navController.navigate("contacts") {
                        launchSingleTop = true
                    }
                },

                notas = noteViewModel.notas
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

        composable("noteDetail") {

            val nota = notaSeleccionada

            if (nota != null) {

                NoteDetailScreen(
                    nota = nota,

                    onBackClick = {
                        noteViewModel.limpiarError()
                        navController.popBackStack()
                    },

                    onEditClick = { titulo, contenido ->

                        noteViewModel.editarNota(
                            idNota = nota.id,
                            titulo = titulo,
                            contenido = contenido,
                            onSuccess = {
                                notaSeleccionada = null
                                navController.popBackStack()
                            }
                        )
                    },

                    onDeleteClick = {

                        noteViewModel.eliminarNota(
                            idNota = nota.id,
                            onSuccess = {
                                notaSeleccionada = null
                                navController.popBackStack()
                            }
                        )
                    },

                    cargando = noteViewModel.cargando,
                    mensajeError = noteViewModel.mensajeError
                )
            }
        }

        composable("contacts") {

            ContactsScreen(
                onBackClick = {
                    navController.popBackStack()
                },

                onNewContactClick = {
                    contactViewModel.limpiarError()
                    navController.navigate("newContact")
                }
            )
        }

        composable("newContact") {

            val context = LocalContext.current

            LaunchedEffect(contactViewModel.mensajeError) {
                contactViewModel.mensajeError?.let { mensaje ->
                    Toast.makeText(
                        context,
                        mensaje,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            NewContactScreen(
                onSaveClick = { nombre, telefono, correo ->

                    contactViewModel.guardarContacto(
                        nombre = nombre,
                        telefono = telefono,
                        correo = correo,
                        onSuccess = {
                            navController.popBackStack()
                        }
                    )
                },

                onCancelClick = {
                    contactViewModel.limpiarError()
                    navController.popBackStack()
                }
            )
        }
    }
}