package com.example.misnotasdecampo
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var imgPreview: ImageView
    private lateinit var btnFoto: Button
    private lateinit var btnGuardar: Button
    private lateinit var edtTitulo: EditText
    private lateinit var edtDescripcion: EditText // Nuevo

    private var fotoCapturada: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        // Enlazamos las vistas
        imgPreview = findViewById(R.id.imgPreview)
        btnFoto = findViewById(R.id.btnTomarFoto)
        btnGuardar = findViewById(R.id.btnGuardar)
        edtTitulo = findViewById(R.id.edtTitulo)
        edtDescripcion = findViewById(R.id.edtDescripcion) // Nuevo

        // Lógica de cámara (Igual que antes)
        val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                fotoCapturada = bitmap
                imgPreview.setImageBitmap(bitmap)
            }
        }
        val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) cameraLauncher.launch(null)
        }

        btnFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(null)
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        // Lógica de guardar la nota
        btnGuardar.setOnClickListener {
            val titulo = edtTitulo.text.toString()
            val descripcion = edtDescripcion.text.toString()

            if (titulo.isNotEmpty()) {
                // 1. Guardar la imagen si existe
                var nombreArchivo: String? = null
                if (fotoCapturada != null) {
                    nombreArchivo = guardarImagenEnDispositivo(fotoCapturada!!)
                }

                // --- NUEVO: Guardar en el Almacén Global ---
                val nuevaNota = Nota(titulo, descripcion, nombreArchivo)
                AlmacenNotas.listaNotas.add(nuevaNota)
                // -------------------------------------------
                AlmacenNotas.guardarNotas(this)
                // 2. Devolver datos al Home (Esto lo dejamos igual para que tu Home siga funcionando)
                val data = Intent()
                data.putExtra("titulo", titulo)
                data.putExtra("descripcion", descripcion)
                data.putExtra("nombre_archivo_foto", nombreArchivo)

                setResult(RESULT_OK, data)
                finish()
            } else {
                edtTitulo.error = "Falta el título"
            }
        }
    }

    // Función auxiliar para guardar el Bitmap como archivo .jpg
    private fun guardarImagenEnDispositivo(bitmap: Bitmap): String {
        val nombreArchivo = "img_${System.currentTimeMillis()}.jpg"
        // Abrimos un archivo privado en la app
        openFileOutput(nombreArchivo, Context.MODE_PRIVATE).use { stream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        }
        return nombreArchivo // Devolvemos el nombre para buscarla luego
    }
}