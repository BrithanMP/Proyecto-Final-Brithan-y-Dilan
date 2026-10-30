package com.brithandilan.notascontactos.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.brithandilan.notascontactos.model.Contact

@Composable
fun ContactDetailScreen(
    contacto: Contact,
    onBackClick: () -> Unit,
    onEditClick: (String, String, String) -> Unit = { _, _, _ -> },
    onDeleteClick: () -> Unit = {},
    cargando: Boolean = false,
    mensajeError: String? = null
) {
    val verdePrincipal = Color(0xFF176B61)
    val context = LocalContext.current

    var modoEdicion by remember(contacto.id) {
        mutableStateOf(false)
    }

    var nombreEditado by remember(contacto.id) {
        mutableStateOf(contacto.nombre)
    }

    var telefonoEditado by remember(contacto.id) {
        mutableStateOf(contacto.telefono)
    }

    var correoEditado by remember(contacto.id) {
        mutableStateOf(contacto.correo)
    }

    var mostrarConfirmacion by remember(contacto.id) {
        mutableStateOf(false)
    }

    var errorFormulario by remember(contacto.id) {
        mutableStateOf<String?>(null)
    }

    var errorComunicacion by remember(contacto.id) {
        mutableStateOf<String?>(null)
    }

    val fecha = if (contacto.fechaCreacion > 0L) {
        android.text.format.DateFormat.format(
            "dd/MM/yyyy HH:mm",
            contacto.fechaCreacion
        ).toString()
    } else {
        "Fecha no disponible"
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = {
                if (!cargando) {
                    mostrarConfirmacion = false
                }
            },
            title = {
                Text("Eliminar contacto")
            },
            text = {
                Text("¿Estás seguro de que deseas eliminar este contacto?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                        onDeleteClick()
                    },
                    enabled = !cargando
                ) {
                    Text(
                        text = "Eliminar",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                    },
                    enabled = !cargando
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        TextButton(
            onClick = onBackClick,
            enabled = !cargando
        ) {
            Text("← Volver a mis contactos")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (modoEdicion) {

            Text(
                text = "Editar contacto",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = verdePrincipal
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nombreEditado,
                onValueChange = { nuevoValor ->
                    telefonoEditado = nuevoValor.filter { it.isDigit() }
                    errorFormulario = null
                },
                label = {
                    Text("Nombre completo")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !cargando
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = telefonoEditado,
                onValueChange = {
                    telefonoEditado = it
                    errorFormulario = null
                },
                label = {
                    Text("Número de teléfono")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                singleLine = true,
                enabled = !cargando
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = correoEditado,
                onValueChange = {
                    correoEditado = it
                    errorFormulario = null
                },
                label = {
                    Text("Correo electrónico (opcional)")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                enabled = !cargando
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    when {
                        nombreEditado.isBlank() ||
                                telefonoEditado.isBlank() -> {
                            errorFormulario = "Completa los campos obligatorios"
                        }

                        telefonoEditado.count { it.isDigit() } < 8 -> {
                            errorFormulario = "Ingresa un teléfono válido"
                        }

                        correoEditado.isNotBlank() &&
                                !Patterns.EMAIL_ADDRESS.matcher(
                                    correoEditado.trim()
                                ).matches() -> {
                            errorFormulario = "Ingresa un correo válido"
                        }

                        else -> {
                            errorFormulario = null

                            onEditClick(
                                nombreEditado.trim(),
                                telefonoEditado.trim(),
                                correoEditado.trim()
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando
            ) {
                Text(
                    if (cargando) "Guardando..." else "Guardar cambios"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    nombreEditado = contacto.nombre
                    telefonoEditado = contacto.telefono
                    correoEditado = contacto.correo
                    errorFormulario = null
                    modoEdicion = false
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando
            ) {
                Text("Cancelar")
            }

        } else {

            Text(
                text = "Detalle del contacto",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = verdePrincipal
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Nombre completo",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = contacto.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 20.dp)
            )

            Text(
                text = "Número de teléfono",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = contacto.telefono,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_DIAL,
                        Uri.fromParts(
                            "tel",
                            contacto.telefono.trim(),
                            null
                        )
                    )

                    try {
                        errorComunicacion = null
                        context.startActivity(intent)
                    } catch (error: ActivityNotFoundException) {
                        errorComunicacion =
                            "No hay una aplicación disponible para llamar"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = contacto.telefono.isNotBlank() && !cargando
            ) {
                Text("Llamar")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_SENDTO,
                        Uri.fromParts(
                            "smsto",
                            contacto.telefono.trim(),
                            null
                        )
                    )

                    try {
                        errorComunicacion = null
                        context.startActivity(intent)
                    } catch (error: ActivityNotFoundException) {
                        errorComunicacion =
                            "No hay una aplicación de mensajes disponible"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = contacto.telefono.isNotBlank() && !cargando
            ) {
                Text("Enviar SMS")
            }

            if (errorComunicacion != null) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = errorComunicacion ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 20.dp)
            )

            Text(
                text = "Correo electrónico",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = contacto.correo.ifBlank {
                    "No registrado"
                },
                style = MaterialTheme.typography.bodyLarge
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 20.dp)
            )

            Text(
                text = "Fecha de registro",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = fecha,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    nombreEditado = contacto.nombre
                    telefonoEditado = contacto.telefono
                    correoEditado = contacto.correo
                    modoEdicion = true
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando
            ) {
                Text("Editar contacto")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    mostrarConfirmacion = true
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando
            ) {
                Text(
                    text = "Eliminar contacto",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        val error = errorFormulario ?: mensajeError

        if (error != null) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}