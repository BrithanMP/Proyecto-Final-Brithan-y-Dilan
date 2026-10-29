package com.brithandilan.notascontactos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.brithandilan.notascontactos.model.Note

@Composable
fun NoteDetailScreen(
    nota: Note,
    onBackClick: () -> Unit,
    onEditClick: (String, String) -> Unit = { _, _ -> },
    onDeleteClick: () -> Unit = {},
    cargando: Boolean = false,
    mensajeError: String? = null
) {
    val verdePrincipal = Color(0xFF176B61)

    var modoEdicion by remember(nota.id) {
        mutableStateOf(false)
    }

    var tituloEditado by remember(nota.id) {
        mutableStateOf(nota.titulo)
    }

    var contenidoEditado by remember(nota.id) {
        mutableStateOf(nota.contenido)
    }

    var mostrarConfirmacion by remember(nota.id) {
        mutableStateOf(false)
    }

    val fecha = if (nota.fechaCreacion > 0L) {
        android.text.format.DateFormat.format(
            "dd/MM/yyyy HH:mm",
            nota.fechaCreacion
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
                Text("Eliminar nota")
            },
            text = {
                Text("¿Estás seguro de que deseas eliminar esta nota?")
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
            Text("← Volver a mis notas")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (modoEdicion) {

            Text(
                text = "Editar nota",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = verdePrincipal
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = tituloEditado,
                onValueChange = {
                    tituloEditado = it
                },
                label = {
                    Text("Título")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !cargando
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contenidoEditado,
                onValueChange = {
                    contenidoEditado = it
                },
                label = {
                    Text("Contenido")
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5,
                enabled = !cargando
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onEditClick(
                        tituloEditado.trim(),
                        contenidoEditado.trim()
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando &&
                        tituloEditado.isNotBlank() &&
                        contenidoEditado.isNotBlank()
            ) {
                Text(
                    if (cargando) "Guardando..." else "Guardar cambios"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    tituloEditado = nota.titulo
                    contenidoEditado = nota.contenido
                    modoEdicion = false
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando
            ) {
                Text("Cancelar")
            }

        } else {

            Text(
                text = nota.titulo,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = verdePrincipal
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Creada: $fecha",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 20.dp)
            )

            Text(
                text = nota.contenido,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    tituloEditado = nota.titulo
                    contenidoEditado = nota.contenido
                    modoEdicion = true
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando
            ) {
                Text("Editar nota")
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
                    text = "Eliminar nota",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        if (mensajeError != null) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}