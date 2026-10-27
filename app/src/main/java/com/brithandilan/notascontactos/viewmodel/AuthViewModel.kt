
package com.brithandilan.notascontactos.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.brithandilan.notascontactos.data.AuthRepository

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()
    fun haySesionActiva(): Boolean {
        return repository.haySesionActiva()
    }


    var cargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    fun registrarUsuario(
        correo: String,
        contrasena: String,
        onSuccess: () -> Unit
    ) {
        if (cargando) return

        cargando = true
        mensajeError = null

        repository.registrarUsuario(
            correo,
            contrasena
        ) { exitoso, error ->

            cargando = false

            if (exitoso) {
                onSuccess()
            } else {
                mensajeError = error
                    ?: "No se pudo registrar el usuario"
            }
        }
    }

    fun iniciarSesion(
        correo: String,
        contrasena: String,
        onSuccess: () -> Unit
    ) {
        if (cargando) return

        cargando = true
        mensajeError = null

        repository.iniciarSesion(
            correo,
            contrasena
        ) { exitoso, error ->

            cargando = false

            if (exitoso) {
                onSuccess()
            } else {
                mensajeError = error
                    ?: "No se pudo iniciar sesión"
            }
        }
    }

    fun limpiarError() {
        mensajeError = null
    }

    fun cerrarSesion() {
        repository.cerrarSesion()
        mensajeError = null
        cargando = false
    }

}
