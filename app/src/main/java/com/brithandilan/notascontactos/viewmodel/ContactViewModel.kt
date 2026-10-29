package com.brithandilan.notascontactos.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.brithandilan.notascontactos.data.ContactRepository

class ContactViewModel : ViewModel() {

    private val repository = ContactRepository()

    var cargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    fun guardarContacto(
        nombre: String,
        telefono: String,
        correo: String,
        onSuccess: () -> Unit
    ) {
        if (cargando) return

        if (nombre.isBlank() || telefono.isBlank()) {
            mensajeError = "Completa los campos obligatorios"
            return
        }

        mensajeError = null
        cargando = true

        repository.guardarContacto(
            nombre = nombre,
            telefono = telefono,
            correo = correo
        ) { exito, error ->

            cargando = false

            if (exito) {
                mensajeError = null
                onSuccess()
            } else {
                mensajeError = error ?: "No se pudo guardar el contacto"
            }
        }
    }

    fun limpiarError() {
        mensajeError = null
    }
}