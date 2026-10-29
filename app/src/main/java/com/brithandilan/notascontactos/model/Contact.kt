package com.brithandilan.notascontactos.model

data class Contact(
    val id: String = "",
    val nombre: String = "",
    val telefono: String = "",
    val correo: String = "",
    val fechaCreacion: Long = 0L
)