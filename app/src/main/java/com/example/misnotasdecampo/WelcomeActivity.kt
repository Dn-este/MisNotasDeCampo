package com.example.misnotasdecampo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Buscamos el botón por su ID del XML
        val btnStart = findViewById<Button>(R.id.btnComenzar)

        btnStart.setOnClickListener {
            // Navegar al Home (MainActivity)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Opcional: Cierra la bienvenida para no volver a ella
        }
    }
}