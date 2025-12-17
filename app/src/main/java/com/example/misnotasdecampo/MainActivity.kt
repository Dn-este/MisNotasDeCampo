package com.example.misnotasdecampo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var contenedorNotas: LinearLayout
    private lateinit var spinnerCategorias: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Inicializar las vistas desde el layout
        val btnNew = findViewById<Button>(R.id.btnNuevaNota)
        spinnerCategorias = findViewById(R.id.category_spinner)
        contenedorNotas = findViewById(R.id.contenedorNotas)

        // 2. Cargar las notas desde el almacenamiento interno
        AlmacenNotas.cargarNotas(this)

        // 3. Configurar el botón para crear una nueva nota
        btnNew.setOnClickListener {
            val intent = Intent(this, CreateNoteActivity::class.java)
            startActivity(intent)
        }

        // 4. Configurar el listener para el Spinner de categorías
        configurarSpinner()
    }

    // onResume se ejecuta cada vez que la actividad se vuelve visible para el usuario.
    // Es el lugar ideal para refrescar la lista.
    override fun onResume() {
        super.onResume()
        // Cuando volvemos a esta pantalla, refrescamos la lista de notas
        // respetando la categoría que esté seleccionada en el Spinner.
        refrescarListaFiltrada()
    }

    private fun configurarSpinner() {
        spinnerCategorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // Este método se dispara cuando el usuario selecciona un ítem del Spinner
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Cada vez que cambia la selección, refrescamos la lista de notas.
                refrescarListaFiltrada()
            }

            // No necesitamos hacer nada si no se selecciona nada.
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Se puede dejar vacío.
            }
        }
    }

    private fun refrescarListaFiltrada() {
        // 1. Obtenemos la categoría que está seleccionada en el Spinner.
        val categoriaSeleccionada = spinnerCategorias.selectedItem.toString()

        // 2. Borramos todas las notas que se están mostrando actualmente para evitar duplicados.
        contenedorNotas.removeAllViews()

        // 3. Determinamos qué lista de notas vamos a mostrar.
        val notasAMostrar = if (categoriaSeleccionada == "Todas") {
            // Si la categoría es "Todas", usamos la lista completa.
            AlmacenNotas.listaNotas
        } else {
            // Si es otra categoría, filtramos la lista para obtener solo las notas que coincidan.
            // Asume que tu clase Nota tiene una propiedad `categoria` de tipo String.
            AlmacenNotas.listaNotas.filter { it.categoria == categoriaSeleccionada }
        }

        // 4. Recorremos la lista filtrada y dibujamos cada nota en la pantalla.
        for (nota in notasAMostrar) {
            agregarNotaVisualmente(nota)
        }
    }

    private fun agregarNotaVisualmente(nota: Nota) {
        val textoView = TextView(this).apply {
            text = "• ${nota.titulo}"
            textSize = 18f
            setPadding(40, 40, 40, 40)
            setBackgroundColor(Color.parseColor("#F0F0F0"))

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 20)
            layoutParams = params

            // Clic corto: Ver detalle
            setOnClickListener {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("titulo_detalle", nota.titulo)
                intent.putExtra("desc_detalle", nota.descripcion)
                intent.putExtra("foto_detalle", nota.rutaImagen)
                // Es importante pasar también el ID para futuras ediciones o borrados
                intent.putExtra("id_detalle", nota.id.toString())
                startActivity(intent)
            }

            // Clic largo: Eliminar
            setOnLongClickListener {
                val builder = android.app.AlertDialog.Builder(this@MainActivity)
                builder.setTitle("¿Eliminar nota?")
                builder.setMessage("Se borrará la nota '${nota.titulo}'. Esta acción no se puede deshacer.")
                builder.setPositiveButton("Eliminar") { dialog, _ ->
                    // Lógica de borrado
                    AlmacenNotas.listaNotas.remove(nota) // Borra de la lista en memoria
                    AlmacenNotas.guardarNotas(this@MainActivity) // Actualiza el archivo en disco
                    refrescarListaFiltrada() // Redibuja la pantalla con la lista actualizada

                    dialog.dismiss()
                }
                builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
                builder.show()
                true // Indica que el evento de clic largo ha sido consumido.
            }
        }

        contenedorNotas.addView(textoView)
    }

    // ¡Ya no necesitas `refrescarListaCompleta()`! La hemos reemplazado por `refrescarListaFiltrada()`.
}
