package com.brithandilan.notascontactos.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.brithandilan.notascontactos.data.ContactRepository
import com.brithandilan.notascontactos.model.Contact

class ContactViewModel : ViewModel() {

    private val repository = ContactRepository()

    var contactos by mutableStateOf<List<Contact>>(emptyList())
        private set

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

    fun cargarContactos() {
        cargando = true
        mensajeError = null

        repository.obtenerContactos { listaContactos, error ->

            cargando = false

            if (error == null) {
                contactos = listaContactos
            } else {
                mensajeError = error
            }
        }
    }

    fun editarContacto(
        idContacto: String,
        nombre: String,
        telefono: String,
        correo: String,
        onSuccess: () -> Unit
    ) {
        if (cargando) return

        if (idContacto.isBlank()) {
            mensajeError = "Contacto no válido"
            return
        }

        if (nombre.isBlank() || telefono.isBlank()) {
            mensajeError = "Completa los campos obligatorios"
            return
        }

        if (telefono.count { it.isDigit() } < 8) {
            mensajeError = "Ingresa un teléfono válido"
            return
        }

        if (correo.isNotBlank() &&
            !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()
        ) {
            mensajeError = "Ingresa un correo válido"
            return
        }

        mensajeError = null
        cargando = true

        repository.editarContacto(
            idContacto = idContacto,
            nombre = nombre,
            telefono = telefono,
            correo = correo
        ) { exito, error ->

            cargando = false

            if (exito) {
                contactos = contactos.map { contacto ->
                    if (contacto.id == idContacto) {
                        contacto.copy(
                            nombre = nombre.trim(),
                            telefono = telefono.trim(),
                            correo = correo.trim()
                        )
                    } else {
                        contacto
                    }
                }.sortedBy { it.nombre.lowercase() }

                mensajeError = null
                onSuccess()
            } else {
                mensajeError = error ?: "No se pudo editar el contacto"
            }
        }
    }

    fun eliminarContacto(
        idContacto: String,
        onSuccess: () -> Unit
    ) {
        if (cargando) return

        if (idContacto.isBlank()) {
            mensajeError = "Contacto no válido"
            return
        }

        mensajeError = null
        cargando = true

        repository.eliminarContacto(idContacto) { exito, error ->

            cargando = false

            if (exito) {
                contactos = contactos.filterNot { contacto ->
                    contacto.id == idContacto
                }

                mensajeError = null
                onSuccess()
            } else {
                mensajeError = error ?: "No se pudo eliminar el contacto"
            }
        }
    }

    fun limpiarError() {
        mensajeError = null
    }
}