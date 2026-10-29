package com.brithandilan.notascontactos.data

import android.util.Patterns
import com.brithandilan.notascontactos.model.Contact
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ContactRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun guardarContacto(
        nombre: String,
        telefono: String,
        correo: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            onResult(false, "Usuario no autenticado")
            return
        }

        if (nombre.isBlank() || telefono.isBlank()) {
            onResult(false, "Completa los campos obligatorios")
            return
        }

        if (telefono.count { it.isDigit() } < 8) {
            onResult(false, "Ingresa un teléfono válido")
            return
        }

        if (correo.isNotBlank() &&
            !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()
        ) {
            onResult(false, "Ingresa un correo válido")
            return
        }

        val contactosRef = db.collection("users")
            .document(userId)
            .collection("contacts")

        val documento = contactosRef.document()

        val contacto = Contact(
            id = documento.id,
            nombre = nombre.trim(),
            telefono = telefono.trim(),
            correo = correo.trim(),
            fechaCreacion = System.currentTimeMillis()
        )

        documento.set(contacto)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { error ->
                onResult(
                    false,
                    error.message ?: "Error al guardar el contacto"
                )
            }
    }

    fun obtenerContactos(
        onResult: (List<Contact>, String?) -> Unit
    ) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            onResult(emptyList(), "Usuario no autenticado")
            return
        }

        db.collection("users")
            .document(userId)
            .collection("contacts")
            .get()
            .addOnSuccessListener { documentos ->

                val contactos = documentos.mapNotNull { documento ->
                    documento.toObject(Contact::class.java)
                }.sortedBy { it.nombre.lowercase() }

                onResult(contactos, null)
            }
            .addOnFailureListener { error ->
                onResult(
                    emptyList(),
                    error.message ?: "Error al obtener los contactos"
                )
            }
    }
}