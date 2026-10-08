
package com.example.miprimeraaplicacion.views

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miprimeraaplicacion.R

class PreferenciasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_preferencias)

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

        // Switch para activar alertas del timbre
        val switchAlertas = findViewById<Switch>(
            R.id.switchAlertas
        )

        // Spinner para seleccionar ubicación
        val spinnerUbicacion = findViewById<Spinner>(
            R.id.spinnerUbicacion
        )

        val ubicaciones = arrayOf(
            "Entrada principal",
            "Portón",
            "Patio",
            "Puerta trasera"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            ubicaciones
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerUbicacion.adapter = adapter

        // Guardar las preferencias localmente
        val preferencias = getSharedPreferences(
            "preferencias_timbre",
            MODE_PRIVATE
        )

        switchAlertas.isChecked = preferencias.getBoolean(
            "alertas",
            false
        )

        val ubicacionGuardada = preferencias.getString(
            "ubicacion",
            "Entrada principal"
        )

        val posicion = ubicaciones.indexOf(ubicacionGuardada)
        if (posicion >= 0) {
            spinnerUbicacion.setSelection(posicion)
        }

        findViewById<android.widget.Button>(
            R.id.btnGuardarPreferencias
        ).setOnClickListener {

            preferencias.edit()
                .putBoolean(
                    "alertas",
                    switchAlertas.isChecked
                )
                .putString(
                    "ubicacion",
                    spinnerUbicacion.selectedItem.toString()
                )
                .apply()

            Toast.makeText(
                this,
                "Preferencias guardadas correctamente",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
