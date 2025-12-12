package com.example.misnotasdecampo

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // 1. Enlazar vistas
        val tvTitulo = findViewById<TextView>(R.id.txtDetalleTitulo)
        val tvDesc = findViewById<TextView>(R.id.txtDetalleDescripcion)
        val imgFoto = findViewById<ImageView>(R.id.imgDetalleFoto)

        // 2. Recibir datos del Intent
        val titulo = intent.getStringExtra("titulo_detalle")
        val descripcion = intent.getStringExtra("desc_detalle")
        val nombreArchivoFoto = intent.getStringExtra("foto_detalle")

        // 3. Mostrar datos
        tvTitulo.text = titulo
        tvDesc.text = descripcion

        // 4. Cargar la foto desde el archivo guardado
        if (nombreArchivoFoto != null) {
            try {
                // Abrimos el archivo que guardamos antes
                val fileStream = openFileInput(nombreArchivoFoto)
                val bitmap = BitmapFactory.decodeStream(fileStream)
                imgFoto.setImageBitmap(bitmap)
                fileStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}