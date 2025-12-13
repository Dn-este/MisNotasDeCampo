package com.example.misnotasdecampo

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var tvTitulo: TextView
    private lateinit var tvDesc: TextView
    private lateinit var imgFoto: ImageView
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // 1. Enlazar con el diseño XML
        tvTitulo = findViewById(R.id.txtDetalleTitulo)
        tvDesc = findViewById(R.id.txtDetalleDescripcion)
        imgFoto = findViewById(R.id.imgDetalleFoto)
        spinner = findViewById(R.id.spinnerNotas)

        // 2. Preparar los datos para el Spinner
        // Obtenemos solo los títulos de nuestra lista global
        val listaTitulos = AlmacenNotas.listaNotas.map { it.titulo }

        // Creamos el adaptador (el puente entre los datos y el Spinner visual)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaTitulos)
        spinner.adapter = adapter

        // 3. Pre-seleccionar la nota que tocaste en el menú
        val tituloInicial = intent.getStringExtra("titulo_detalle")
        // Buscamos en qué posición de la lista está ese título
        val posicionInicial = AlmacenNotas.listaNotas.indexOfFirst { it.titulo == tituloInicial }

        if (posicionInicial >= 0) {
            spinner.setSelection(posicionInicial)
        }

        // 4. Qué hacer cuando el usuario cambia el Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Buscamos la nota completa usando la posición elegida
                val notaSeleccionada = AlmacenNotas.listaNotas[position]

                // Actualizamos la pantalla con los datos de esa nota
                actualizarPantalla(notaSeleccionada)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun actualizarPantalla(nota: Nota) {
        tvTitulo.text = nota.titulo
        tvDesc.text = nota.descripcion

        // Limpiamos imagen anterior
        imgFoto.setImageDrawable(null)
        imgFoto.setBackgroundColor(android.graphics.Color.parseColor("#CCCCCC"))

        // Cargar imagen si existe
        if (nota.rutaImagen != null) {
            try {
                val fileStream = openFileInput(nota.rutaImagen)
                val bitmap = BitmapFactory.decodeStream(fileStream)
                imgFoto.setImageBitmap(bitmap)
                imgFoto.background = null // Quitamos el fondo gris si hay foto
                fileStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}