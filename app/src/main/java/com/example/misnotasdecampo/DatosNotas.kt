package com.example.misnotasdecampo

// 1. Definimos la estructura de una Nota
data class Nota(
    val titulo: String,
    val descripcion: String,
    val rutaImagen: String?
)

// 2. Creamos un "Almacén" que vive en toda la app
object AlmacenNotas {
    val listaNotas = mutableListOf<Nota>()
}