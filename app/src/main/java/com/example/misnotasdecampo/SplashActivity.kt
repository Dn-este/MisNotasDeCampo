package com.example.misnotasdecampo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Espera 3 segundos y cambia de pantalla
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish() // Cierra el Splash para que no se pueda volver atrás
        }, 3000)
    }
}