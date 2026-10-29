
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

    fun editarNota(
        idNota: String,
        titulo: String,
        contenido: String,
        onSuccess: () -> Unit
    ) {
        if (cargando) return

        if (idNota.isBlank()) {
            mensajeError = "Nota no válida"
            return
        }

        if (titulo.isBlank() || contenido.isBlank()) {
            mensajeError = "Completa todos los campos"
            return
        }

        mensajeError = null
        cargando = true

        repository.editarNota(
            idNota = idNota,
            titulo = titulo,
            contenido = contenido
        ) { exito, error ->

            cargando = false

            if (exito) {
                notas = notas.map { nota ->
                    if (nota.id == idNota) {
                        nota.copy(
                            titulo = titulo.trim(),
                            contenido = contenido.trim()
                        )
                    } else {
                        nota
                    }
                }

                mensajeError = null
                onSuccess()
            } else {
                mensajeError = error ?: "No se pudo editar la nota"
            }
        }
    }

    fun eliminarNota(
        idNota: String,
        onSuccess: () -> Unit
    ) {
        if (cargando) return

        if (idNota.isBlank()) {
            mensajeError = "Nota no válida"
            return
        }

        mensajeError = null
        cargando = true

        repository.eliminarNota(idNota) { exito, error ->

            cargando = false

            if (exito) {
                notas = notas.filterNot { nota ->
                    nota.id == idNota
                }

                mensajeError = null
                onSuccess()
            } else {
                mensajeError = error ?: "No se pudo eliminar la nota"
            }
        }
    }

    fun limpiarError() {
        mensajeError = null
    }
}