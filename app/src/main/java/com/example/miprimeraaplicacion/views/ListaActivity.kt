
package com.example.miprimeraaplicacion.views

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miprimeraaplicacion.R
import com.example.miprimeraaplicacion.adapters.EventoTimbreAdapter
import com.example.miprimeraaplicacion.models.EventoTimbre
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ListaActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: EventoTimbreAdapter
    private lateinit var rvLecturas: RecyclerView

    private var listenerEventos: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        db = FirebaseFirestore.getInstance()

        adapter = EventoTimbreAdapter(
            onEditarClick = { evento ->

                val intent = Intent(
                    this,
                    FormularioLecturaActivity::class.java
                )

                intent.putExtra("eventoId", evento.id)
                intent.putExtra("eventoUbicacion", evento.ubicacion)
                intent.putExtra("eventoTipo", evento.tipoEvento)
                intent.putExtra("eventoDuracion", evento.duracionSonido)
                intent.putExtra("eventoFecha", evento.fechaHora)

                startActivity(intent)
            },

            onEliminarClick = { evento ->
                mostrarDialogoConfirmacion(evento)
            }
        )

        rvLecturas = findViewById(R.id.rvLecturas)
        rvLecturas.layoutManager = LinearLayoutManager(this)
        rvLecturas.adapter = adapter

        val fabAgregar = findViewById<FloatingActionButton>(
            R.id.fabAgregarLectura
        )

        fabAgregar.setOnClickListener {
            val intent = Intent(
                this,
                FormularioLecturaActivity::class.java
            )
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        escucharEventos()
    }

    private fun escucharEventos() {

        listenerEventos?.remove()

        listenerEventos = db.collection("eventos_timbre")
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Log.e(
                        "ListaActivity",
                        "Error al obtener eventos",
                        error
                    )

                    Toast.makeText(
                        this,
                        "Error al cargar los eventos",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addSnapshotListener
                }

                if (snapshot != null) {

                    val listaEventos = snapshot.toObjects(
                        EventoTimbre::class.java
                    )

                    adapter.actualizarLista(listaEventos)

                    Log.d(
                        "ListaActivity",
                        "Eventos recuperados: ${listaEventos.size}"
                    )
                }
            }
    }

    private fun mostrarDialogoConfirmacion(
        evento: EventoTimbre
    ) {

        AlertDialog.Builder(this)
            .setTitle("Eliminar evento")
            .setMessage(
                "¿Deseas eliminar el evento de ${evento.ubicacion}?"
            )
            .setPositiveButton("Sí, eliminar") { _, _ ->
                eliminarEvento(evento)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarEvento(evento: EventoTimbre) {

        if (evento.id.isBlank()) {
            Toast.makeText(
                this,
                "No se encontró el ID del evento",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        db.collection("eventos_timbre")
            .document(evento.id)
            .delete()
            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Evento eliminado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { error ->

                Log.e(
                    "ListaActivity",
                    "Error al eliminar evento",
                    error
                )

                Toast.makeText(
                    this,
                    "No se pudo eliminar el evento",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onStop() {
        listenerEventos?.remove()
        listenerEventos = null
        super.onStop()
    }
}
