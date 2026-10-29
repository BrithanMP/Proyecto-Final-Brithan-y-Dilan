
package com.brithandilan.notascontactos.data

import com.brithandilan.notascontactos.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NoteRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun guardarNota(
        titulo: String,
        contenido: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            onResult(false, "Usuario no autenticado")
            return
        }

        if (titulo.isBlank() || contenido.isBlank()) {
            onResult(false, "Completa todos los campos")
            return
        }

        val notasRef = db.collection("users")
            .document(userId)
            .collection("notes")

        val documento = notasRef.document()

        val nota = Note(
            id = documento.id,
            titulo = titulo.trim(),
            contenido = contenido.trim(),
            fechaCreacion = System.currentTimeMillis()
        )

        documento.set(nota)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { error ->
                onResult(false, error.message ?: "Error al guardar la nota")
            }
    }
}
