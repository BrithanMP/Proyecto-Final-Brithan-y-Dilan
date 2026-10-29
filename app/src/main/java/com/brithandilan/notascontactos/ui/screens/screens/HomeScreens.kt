
package com.brithandilan.notascontactos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.brithandilan.notascontactos.model.Note
import com.brithandilan.notascontactos.ui.components.NoteCard

@Composable
fun HomeScreen(
    onLogoutClick: () -> Unit,
    onNewNoteClick: () -> Unit,
    notas: List<Note> = emptyList()
) {
    var busqueda by remember { mutableStateOf("") }

    val verdePrincipal = Color(0xFF176B61)

    // Filtrar las notas por título o contenido
    val notasFiltradas = notas.filter { nota ->
        nota.titulo.contains(
            busqueda,
            ignoreCase = true
        ) || nota.contenido.contains(
            busqueda,
            ignoreCase = true
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // Encabezado de Home
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

            // Cerrar sesión
            IconButton(onClick = onLogoutClick) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Cerrar sesión"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Crear nueva nota
        Button(
            onClick = onNewNoteClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nueva nota")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Buscar notas
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

        // Mostrar notas obtenidas desde Firebase
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
                items(
                    items = notasFiltradas,
                    key = { nota -> nota.id }
                ) { nota ->

                    // Componente visual reutilizable
                    NoteCard(nota = nota)
                }
            }
        }
    }
}
