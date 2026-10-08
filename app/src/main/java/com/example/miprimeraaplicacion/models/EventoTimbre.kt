
package com.example.miprimeraaplicacion.models

import com.google.firebase.firestore.DocumentId

data class EventoTimbre(

    @DocumentId
    var id: String = "",

    var ubicacion: String = "",

    var tipoEvento: String = "",

    var duracionSonido: Double = 0.0,

    var fechaHora: String = ""

)
