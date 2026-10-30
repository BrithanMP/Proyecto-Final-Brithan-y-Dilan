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
import com.brithandilan.notascontactos.model.Contact

@Composable
fun ContactDetailScreen(
    contacto: Contact,
    onBackClick: () -> Unit
) {
    val verdePrincipal = Color(0xFF176B61)

    val fecha = if (contacto.fechaCreacion > 0L) {
        android.text.format.DateFormat.format(
            "dd/MM/yyyy HH:mm",
            contacto.fechaCreacion
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
            Text("← Volver a mis contactos")
        }

        Spacer(modifier = Modifier.height(24.dp))

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
    }
}