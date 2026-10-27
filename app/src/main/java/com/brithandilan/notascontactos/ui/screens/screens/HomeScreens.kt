
package com.brithandilan.notascontactos.ui.screens

import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class NotaMuestra(
    val titulo: String,
    val contenido: String
)

@Composable

fun HomeScreen(
    onLogoutClick: () -> Unit,
    onNewNoteClick: () -> Unit
)
 {
    var busqueda by remember { mutableStateOf("") }

     Spacer(modifier = Modifier.height(16.dp))

     Button(
         onClick = onNewNoteClick,
         modifier = Modifier.fillMaxWidth()
     ) {
         Text("Nueva nota")
     }

     Spacer(modifier = Modifier.height(24.dp))


     val notas = listOf(
        NotaMuestra(
            titulo = "Ideas para el proyecto",
            contenido = "Organizar las tareas pendientes y definir las próximas prioridades."
        ),
        NotaMuestra(
            titulo = "Lista de pendientes",
            contenido = "Revisar las actividades de la semana y preparar los próximos objetivos."
        ),
        NotaMuestra(
            titulo = "Recordatorios",
            contenido = "Anotar las cosas importantes para no olvidarlas."
        )
    )


    val notasFiltradas = notas.filter {
        it.titulo.contains(busqueda, ignoreCase = true) ||
                it.contenido.contains(busqueda, ignoreCase = true)
    }

    val verdePrincipal = Color(0xFF176B61)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Mis notas",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = verdePrincipal
                )

                Text(
                    text = "Tus ideas, siempre a mano",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(onClick = onLogoutClick) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Cerrar sesión"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = {
                Text("Buscar entre mis notas")
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
            text = "Notas recientes",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (notasFiltradas.isEmpty()) {
            Text(
                text = "No se encontraron notas.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notasFiltradas) { nota ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NoteAlt,
                                contentDescription = null,
                                tint = verdePrincipal
                            )

                            Column {
                                Text(
                                    text = nota.titulo,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(
                                    modifier = Modifier.height(6.dp)
                                )

                                Text(
                                    text = nota.contenido,
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

