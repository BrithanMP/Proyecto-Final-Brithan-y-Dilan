
package com.brithandilan.notascontactos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String) -> Unit,
    onBackToLoginClick: () -> Unit,
    mensajeError: String? = null,
    cargando: Boolean = false
)
 {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorNombre by remember { mutableStateOf("") }
    var errorCorreo by remember { mutableStateOf("") }
    var errorContrasena by remember { mutableStateOf("") }


    val verdePrincipal = Color(0xFF176B61)
    val fondoTarjeta = Color(0xFFF4F7F6)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Notas y Contactos",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = verdePrincipal
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Crea tu cuenta y comienza a organizarte",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = fondoTarjeta
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Crear cuenta",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))


                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        errorNombre = ""
                    },
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    isError = errorNombre.isNotEmpty(),
                    supportingText = {
                        if (errorNombre.isNotEmpty()) {
                            Text(errorNombre)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = correo,
                    onValueChange = {
                        correo = it
                        errorCorreo = ""
                    },
                    label = { Text("Correo electrónico") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    singleLine = true,
                    isError = errorCorreo.isNotEmpty(),
                    supportingText = {
                        if (errorCorreo.isNotEmpty()) {
                            Text(errorCorreo)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = contrasena,
                    onValueChange = {
                        contrasena = it
                        errorContrasena = ""
                    },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    singleLine = true,
                    isError = errorContrasena.isNotEmpty(),
                    supportingText = {
                        if (errorContrasena.isNotEmpty()) {
                            Text(errorContrasena)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(modifier = Modifier.height(24.dp))

                Button(

                    onClick = {
                        errorNombre = ""
                        errorCorreo = ""
                        errorContrasena = ""

                        if (nombre.isBlank()) {
                            errorNombre = "El nombre es obligatorio"
                        }

                        if (correo.isBlank()) {
                            errorCorreo = "El correo es obligatorio"
                        } else if (
                            !android.util.Patterns.EMAIL_ADDRESS
                                .matcher(correo.trim()).matches()
                        ) {
                            errorCorreo = "Ingresa un correo válido"
                        }

                        if (contrasena.isBlank()) {
                            errorContrasena = "La contraseña es obligatoria"
                        } else if (contrasena.length < 6) {
                            errorContrasena = "Mínimo 6 caracteres"
                        }

                        if (
                            errorNombre.isEmpty() &&
                            errorCorreo.isEmpty() &&
                            errorContrasena.isEmpty()
                        ) {
                            onRegisterClick(
                                nombre.trim(),
                                correo.trim(),
                                contrasena
                            )
                        }
                    }
                    ,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = verdePrincipal
                    )
                ) {
                    Text("Crear cuenta")
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onBackToLoginClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "¿Ya tienes cuenta? Inicia sesión",
                        color = verdePrincipal
                    )
                }

                if (mensajeError != null) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = mensajeError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (cargando) {
                    Spacer(modifier = Modifier.height(12.dp))

                    CircularProgressIndicator()
                }
            }
        }
    }
}
