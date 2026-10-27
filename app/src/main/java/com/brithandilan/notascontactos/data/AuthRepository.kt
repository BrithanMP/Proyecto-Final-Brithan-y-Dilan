
package com.brithandilan.notascontactos.data

import com.google.firebase.auth.FirebaseAuth

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    fun registrarUsuario(
        correo: String,
        contrasena: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(
            correo,
            contrasena
        ).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                onResult(true, null)
            } else {
                onResult(
                    false,
                    task.exception?.localizedMessage
                        ?: "No se pudo registrar el usuario"
                )
            }
        }
    }

    fun iniciarSesion(
        correo: String,
        contrasena: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(
            correo,
            contrasena
        ).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                onResult(true, null)
            } else {
                onResult(
                    false,
                    task.exception?.localizedMessage
                        ?: "No se pudo iniciar sesión"
                )
            }
        }
    }
}
