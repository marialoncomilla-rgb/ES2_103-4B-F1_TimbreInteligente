
package com.example.miprimeraaplicacion.views

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.miprimeraaplicacion.R
import com.example.miprimeraaplicacion.models.EventoTimbre
import com.google.firebase.firestore.FirebaseFirestore

class FormularioLecturaActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var txtTituloFormulario: TextView
    private lateinit var edtUbicacion: EditText
    private lateinit var edtTipoEvento: EditText
    private lateinit var edtDuracion: EditText
    private lateinit var edtFechaHora: EditText

    private var eventoIdEditar: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_lectura)

        db = FirebaseFirestore.getInstance()

        txtTituloFormulario = findViewById(
            R.id.txtTituloFormulario
        )

        edtUbicacion = findViewById(
            R.id.edtUbicacion
        )

        edtTipoEvento = findViewById(
            R.id.edtTipoEvento
        )

        edtDuracion = findViewById(
            R.id.edtDuracion
        )

        edtFechaHora = findViewById(
            R.id.edtFechaHora
        )

        eventoIdEditar = intent.getStringExtra("eventoId")

        if (eventoIdEditar != null) {

            txtTituloFormulario.text = "Editar evento del timbre"

            edtUbicacion.setText(
                intent.getStringExtra("eventoUbicacion")
            )

            edtTipoEvento.setText(
                intent.getStringExtra("eventoTipo")
            )

            val duracion = intent.getDoubleExtra(
                "eventoDuracion",
                0.0
            )

            edtDuracion.setText(duracion.toString())

            edtFechaHora.setText(
                intent.getStringExtra("eventoFecha")
            )

        } else {

            txtTituloFormulario.text = "Nuevo evento del timbre"
        }
    }

    fun onGuardarLecturaClick(view: View) {

        val ubicacion = edtUbicacion.text.toString().trim()
        val tipoEvento = edtTipoEvento.text.toString().trim()
        val duracionTexto = edtDuracion.text.toString().trim()
        val fechaHora = edtFechaHora.text.toString().trim()

        var valido = true

        if (ubicacion.isEmpty()) {
            edtUbicacion.error = "Ingresa la ubicación"
            valido = false
        }

        if (tipoEvento.isEmpty()) {
            edtTipoEvento.error = "Ingresa el tipo de evento"
            valido = false
        }

        if (duracionTexto.isEmpty()) {
            edtDuracion.error = "Ingresa la duración"
            valido = false
        }

        if (fechaHora.isEmpty()) {
            edtFechaHora.error = "Ingresa la fecha y hora"
            valido = false
        }

        if (!valido) {
            Toast.makeText(
                this,
                "Completa todos los campos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val duracion = duracionTexto.toDoubleOrNull()

        if (duracion == null ||
            !duracion.isFinite() ||
            duracion < 0
        ) {
            edtDuracion.error = "Ingresa una duración válida"
            return
        }

        val evento = EventoTimbre(
            ubicacion = ubicacion,
            tipoEvento = tipoEvento,
            duracionSonido = duracion,
            fechaHora = fechaHora
        )

        val id = eventoIdEditar

        if (id != null) {

            db.collection("eventos_timbre")
                .document(id)
                .set(evento)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Evento actualizado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                }
                .addOnFailureListener { error ->

                    Toast.makeText(
                        this,
                        "Error al actualizar: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

        } else {

            db.collection("eventos_timbre")
                .add(evento)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Evento guardado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                }
                .addOnFailureListener { error ->

                    Toast.makeText(
                        this,
                        "Error al guardar: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}
