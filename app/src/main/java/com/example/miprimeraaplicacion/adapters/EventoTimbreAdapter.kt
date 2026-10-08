
package com.example.miprimeraaplicacion.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.miprimeraaplicacion.R
import com.example.miprimeraaplicacion.models.EventoTimbre

class EventoTimbreAdapter(
    private var listaEventos: List<EventoTimbre> = listOf(),
    private val onEditarClick: (EventoTimbre) -> Unit,
    private val onEliminarClick: (EventoTimbre) -> Unit
) : RecyclerView.Adapter<EventoTimbreAdapter.EventoViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventoViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lectura, parent, false)

        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: EventoViewHolder,
        position: Int
    ) {
        val evento = listaEventos[position]

        holder.bind(
            evento,
            onEditarClick,
            onEliminarClick
        )
    }

    override fun getItemCount(): Int {
        return listaEventos.size
    }

    fun actualizarLista(nuevaLista: List<EventoTimbre>) {
        listaEventos = nuevaLista
        notifyDataSetChanged()
    }

    class EventoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val txtDescripcion: TextView =
            itemView.findViewById(R.id.txtDescripcion)

        private val txtValor: TextView =
            itemView.findViewById(R.id.txtValor)

        private val btnEliminar: ImageButton =
            itemView.findViewById(R.id.btnEliminar)

        fun bind(
            evento: EventoTimbre,
            onEditarClick: (EventoTimbre) -> Unit,
            onEliminarClick: (EventoTimbre) -> Unit
        ) {

            txtDescripcion.text = """
                🔔 Ubicación: ${evento.ubicacion}
                Tipo de evento: ${evento.tipoEvento}
                Fecha: ${evento.fechaHora}
            """.trimIndent()

            txtValor.text =
                "Duración del sonido: ${evento.duracionSonido} segundos"

            itemView.setOnClickListener {
                onEditarClick(evento)
            }

            btnEliminar.setOnClickListener {
                onEliminarClick(evento)
            }
        }
    }
}
