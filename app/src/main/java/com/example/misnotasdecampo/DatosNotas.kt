package com.example.misnotasdecampo

import android.content.Context
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

// 1. Agregamos ": Serializable" para que Android sepa convertir esto a archivo
data class Nota(
    val titulo: String,
    val descripcion: String,
    val rutaImagen: String?
) : Serializable

object AlmacenNotas {
    var listaNotas = mutableListOf<Nota>() // Cambiamos a 'var' para poder reemplazarla entera
    private const val NOMBRE_ARCHIVO = "notas_guardadas.bin"

    // Función para guardar la lista en el disco
    fun guardarNotas(context: Context) {
        try {
            val fos = context.openFileOutput(NOMBRE_ARCHIVO, Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(listaNotas)
            oos.close()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Función para recuperar la lista del disco
    fun cargarNotas(context: Context) {
        try {
            // Verificamos si el archivo existe antes de intentar leer
            if (context.fileList().contains(NOMBRE_ARCHIVO)) {
                val fis = context.openFileInput(NOMBRE_ARCHIVO)
                val ois = ObjectInputStream(fis)
                // Leemos el objeto y lo convertimos (casteamos) a nuestra lista
                listaNotas = ois.readObject() as MutableList<Nota>
                ois.close()
                fis.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}