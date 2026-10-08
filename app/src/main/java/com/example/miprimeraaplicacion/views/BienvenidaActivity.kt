
package com.example.miprimeraaplicacion.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miprimeraaplicacion.R

class BienvenidaActivity : AppCompatActivity() {

    private var nombreUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bienvenida)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        nombreUsuario = intent.getStringExtra("usuario")

        val txtBienvenida = findViewById<TextView>(
            R.id.txtBienvenida
        )

        txtBienvenida.text = """
            ¡Bienvenido, ${nombreUsuario ?: "usuario"}!

            🔔 TIMBRE INTELIGENTE

            Sistema de registro y monitoreo
            de eventos de un timbre inalámbrico.

            Nuestra aplicación permite:

            • Registrar eventos del timbre.
            • Consultar el historial de eventos.
            • Modificar registros existentes.
            • Eliminar eventos registrados.
            • Configurar preferencias de alertas.

            Proyecto IoT
            María José y Dasne
        """.trimIndent()
    }

    fun onPreferenciasClick(view: View) {
        val intent = Intent(
            this,
            PreferenciasActivity::class.java
        )
        intent.putExtra("usuario", nombreUsuario)
        startActivity(intent)
    }

    fun onVerLecturasClick(view: View) {
        val intent = Intent(
            this,
            ListaActivity::class.java
        )
        startActivity(intent)
    }
}
