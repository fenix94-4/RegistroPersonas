package com.example.registropersonas



data class Persona(
    val nombre: String,
    val pais: String,
    val direccion: String,
    val estadocivil: Boolean,
    val trabajo: String,
    val id: String? = null,
    val createdAt: String? = null,
)
