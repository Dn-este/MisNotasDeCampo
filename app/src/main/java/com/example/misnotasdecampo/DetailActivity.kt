package com.example.misnotasdecampo

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var tvTitulo: TextView
    private lateinit var tvDesc: TextView
    private lateinit var imgFoto: ImageView
    private lateinit var spinner: Spinner
    private lateinit var btnVolver: Button
    private lateinit var btnEliminar: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // 1. Enlazar con el diseño XML
        tvTitulo = findViewById(R.id.txtDetalleTitulo)
        tvDesc = findViewById(R.id.txtDetalleDescripcion)
        imgFoto = findViewById(R.id.imgDetalleFoto)
        spinner = findViewById(R.id.spinnerNotas)
        btnVolver = findViewById(R.id.btnVolver)
        btnEliminar = findViewById(R.id.btnEliminar)


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

        // Volver a la pantalla principal
        btnVolver.setOnClickListener {
            val intento = Intent(this, MainActivity::class.java)
            startActivity(intento)
            finish()
        }

            btnEliminar.setOnClickListener {
                mostrarDialogoConfirmacion()
            }
        }

        private fun mostrarDialogoConfirmacion() {
            val posicionActual = spinner.selectedItemPosition

            // Verificamos que la posición sea válida
            if (posicionActual != AdapterView.INVALID_POSITION) {
                AlertDialog.Builder(this)
                    .setTitle("¿Eliminar nota?")
                    .setMessage("Esta acción no se puede deshacer.")
                    .setPositiveButton("Eliminar") { _, _ ->
                        eliminarNota(posicionActual)
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        }

        private fun eliminarNota(posicion: Int) {
            // A. Borrar de la lista en memoria
            AlmacenNotas.listaNotas.removeAt(posicion)

            // B. Actualizar el archivo en el disco para que sea permanente
            AlmacenNotas.guardarNotas(this)

            // C. Cerrar esta pantalla y volver al Main
            finish()
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