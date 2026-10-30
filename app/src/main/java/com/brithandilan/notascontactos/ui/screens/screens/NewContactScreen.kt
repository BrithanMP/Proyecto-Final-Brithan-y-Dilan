package com.brithandilan.notascontactos.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun NewContactScreen(
    onSaveClick: (String, String, String) -> Unit,
    onCancelClick: () -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    val verdePrincipal = Color(0xFF176B61)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        TextButton(
            onClick = onCancelClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = verdePrincipal
            )
        ) {
            Text("← Volver a mis contactos")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Nuevo contacto",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = verdePrincipal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Completa la información del contacto",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                mensajeError = null
            },
            label = {
                Text("Nombre completo")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = telefono,
            onValueChange = { nuevoValor ->
                telefono = nuevoValor.filter { it.isDigit() }
                mensajeError = null
            },
            label = {
                Text("Número de teléfono")
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                mensajeError = null
            },
            label = {
                Text("Correo electrónico (opcional)")
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (mensajeError != null) {
            Text(
                text = mensajeError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                when {
                    nombre.isBlank() || telefono.isBlank() -> {
                        mensajeError = "Completa los campos obligatorios"
                    }

                    telefono.length < 8 -> {
                        mensajeError = "Ingresa un teléfono válido"
                    }

                    correo.isNotBlank() &&
                            !android.util.Patterns.EMAIL_ADDRESS
                                .matcher(correo.trim()).matches() -> {
                        mensajeError = "Ingresa un correo válido"
                    }

                    else -> {
                        mensajeError = null
                        onSaveClick(
                            nombre.trim(),
                            telefono.trim(),
                            correo.trim()
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = verdePrincipal
            )
        ) {
            Text("Guardar contacto")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onCancelClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = verdePrincipal
            ),
            border = BorderStroke(1.dp, verdePrincipal)
        ) {
            Text("Cancelar")
        }
    }
}