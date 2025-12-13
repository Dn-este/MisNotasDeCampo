package com.example.misnotasdecampo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var contenedorNotas: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNew = findViewById<Button>(R.id.btnNuevaNota)
        contenedorNotas = findViewById(R.id.contenedorNotas)

        // 1. Cargar las notas del disco apenas arranca la app
        AlmacenNotas.cargarNotas(this)

        btnNew.setOnClickListener {
            val intent = Intent(this, CreateNoteActivity::class.java)
            startActivity(intent)
        }
    }

    // "onResume" se ejecuta siempre que la pantalla se vuelve visible
    // (Al abrir la app Y al volver de crear una nota)
    override fun onResume() {
        super.onResume()
        refrescarListaCompleta()
    }

    private fun refrescarListaCompleta() {
        // 1. Borramos todo lo que haya en pantalla para no duplicar
        contenedorNotas.removeAllViews()

        // 2. Recorremos la lista del Almacén y dibujamos cada nota
        for (nota in AlmacenNotas.listaNotas) {
            agregarNotaVisualmente(nota)
        }
    }

    private fun agregarNotaVisualmente(nota: Nota) {
        val textoView = TextView(this)
        textoView.text = "• ${nota.titulo}"
        textoView.textSize = 18f
        textoView.setPadding(40, 40, 40, 40)
        textoView.setBackgroundColor(Color.parseColor("#F0F0F0"))

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 20)
        textoView.layoutParams = params

        // Clic corto: Ver detalle
        textoView.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            // Pasamos los datos necesarios
            intent.putExtra("titulo_detalle", nota.titulo)
            intent.putExtra("desc_detalle", nota.descripcion)
            intent.putExtra("foto_detalle", nota.rutaImagen)
            startActivity(intent)
        }

        // Clic largo: Eliminar
        textoView.setOnLongClickListener {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("¿Eliminar nota?")
            builder.setPositiveButton("Eliminar") { dialog, _ ->

                // --- LÓGICA DE BORRADO ---
                AlmacenNotas.listaNotas.remove(nota) // Borrar de memoria
                AlmacenNotas.guardarNotas(this)      // Actualizar el archivo en disco
                refrescarListaCompleta()             // Redibujar la pantalla

                dialog.dismiss()
            }
            builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            builder.show()
            true
        }

        contenedorNotas.addView(textoView)
    }
}