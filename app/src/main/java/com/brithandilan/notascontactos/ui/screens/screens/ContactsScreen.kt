package com.brithandilan.notascontactos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.brithandilan.notascontactos.model.Contact

@Composable
fun ContactsScreen(
    onBackClick: () -> Unit,
    onNewContactClick: () -> Unit,
    contactos: List<Contact> = emptyList(),
    cargando: Boolean = false,
    mensajeError: String? = null
) {
    var busqueda by remember {
        mutableStateOf("")
    }

    val verdePrincipal = Color(0xFF176B61)

    val contactosFiltrados = contactos.filter { contacto ->
        contacto.nombre.contains(busqueda, ignoreCase = true) ||
                contacto.telefono.contains(busqueda, ignoreCase = true) ||
                contacto.correo.contains(busqueda, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        TextButton(
            onClick = onBackClick
        ) {
            Text("← Volver a mis notas")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Mis contactos",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = verdePrincipal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Organiza tus contactos fácilmente",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNewContactClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = verdePrincipal
            )
        ) {
            Text("Nuevo contacto")
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = {
                busqueda = it
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = {
                Text("Buscar contacto")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            },
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Contactos registrados",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (mensajeError != null) {
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        if (cargando) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else if (contactosFiltrados.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (busqueda.isBlank()) {
                        "Aún no tienes contactos registrados."
                    } else {
                        "No se encontraron contactos."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = contactosFiltrados,
                    key = { contacto -> contacto.id }
                ) { contacto ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = contacto.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = verdePrincipal
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Teléfono: ${contacto.telefono}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            if (contacto.correo.isNotBlank()) {

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Correo: ${contacto.correo}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}