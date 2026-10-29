
package com.brithandilan.notascontactos.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.brithandilan.notascontactos.data.NoteRepository
import com.brithandilan.notascontactos.model.Note


class NoteViewModel : ViewModel() {

    private val repository = NoteRepository()


    var notas by mutableStateOf<List<Note>>(emptyList())
        private set

    var cargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    fun guardarNota(
        titulo: String,
        contenido: String,
        onSuccess: () -> Unit
    ) {
        if (cargando) return

        if (titulo.isBlank() || contenido.isBlank()) {
            mensajeError = "Completa todos los campos"
            return
        }

        mensajeError = null
        cargando = true

        repository.guardarNota(
            titulo = titulo,
            contenido = contenido
        ) { exito, error ->

            cargando = false

            if (exito) {
                mensajeError = null
                onSuccess()
            } else {
                mensajeError = error ?: "No se pudo guardar la nota"
            }
        }
    }

    fun limpiarError() {
        mensajeError = null
    }
    fun cargarNotas() {
        cargando = true
        mensajeError = null

        repository.obtenerNotas { listaNotas, error ->
            cargando = false

            if (error == null) {
                notas = listaNotas
            } else {
                mensajeError = error
            }
        }
    }


}
