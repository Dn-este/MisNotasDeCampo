package com.example.misnotasdecampo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var contenedorNotas: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNew = findViewById<Button>(R.id.btnNuevaNota)
        contenedorNotas = findViewById(R.id.contenedorNotas) // Buscamos el contenedor

        // 1. Preparamos el receptor de resultados (Para cuando volvamos de crear nota)
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val titulo = data?.getStringExtra("titulo") ?: ""
                    val descripcion = data?.getStringExtra("descripcion") ?: ""
                    val fotoArchivo = data?.getStringExtra("nombre_archivo_foto")

                    if (titulo.isNotEmpty()) {
                        // Pasamos TODOS los datos a la función de crear la vista
                        agregarNotaVisualmente(titulo, descripcion, fotoArchivo)
                    }
                }
            }

        // 2. Botón Nueva Nota
        btnNew.setOnClickListener {
            val intent = Intent(this, CreateNoteActivity::class.java)
            // Usamos launch en lugar de startActivity simple
            resultLauncher.launch(intent)
        }
    }

    // Función auxiliar para crear un TextView con código
    private fun agregarNotaVisualmente(titulo: String, descripcion: String, fotoArchivo: String?) {
        val textoView = TextView(this)
        textoView.text = "• $titulo"
        textoView.textSize = 18f
        textoView.setPadding(40, 40, 40, 40)
        textoView.setBackgroundColor(Color.parseColor("#F0F0F0"))

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 20)
        textoView.layoutParams = params

        // --- NUEVO COMPORTAMIENTO DE CLIC ---
        textoView.setOnClickListener {
            // Al hacer clic, abrimos el DETALLE enviando los datos
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("titulo_detalle", titulo)
            intent.putExtra("desc_detalle", descripcion)
            intent.putExtra("foto_detalle", fotoArchivo)
            startActivity(intent)
        }

        // Mantenemos el clic largo para borrar
        textoView.setOnLongClickListener {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("¿Eliminar nota?")
            builder.setPositiveButton("Eliminar") { dialog, _ ->
                contenedorNotas.removeView(textoView)
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            builder.show()
            true
        }

        contenedorNotas.addView(textoView)
    }
}