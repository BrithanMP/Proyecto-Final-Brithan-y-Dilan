
package com.brithandilan.notascontactos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.brithandilan.notascontactos.model.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NoteDetailScreen(
    nota: Note,
    onBackClick: () -> Unit
) {
    val verdePrincipal = Color(0xFF176B61)


    val fecha = if (nota.fechaCreacion > 0L) {
        android.text.format.DateFormat.format(
            "dd/MM/yyyy HH:mm",
            nota.fechaCreacion
        ).toString()
    } else {
        "Fecha no disponible"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        TextButton(
            onClick = onBackClick
        ) {
            Text("← Volver a mis notas")
        }

        Spacer(modifier = Modifier.height(24.dp))

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
    }
}
