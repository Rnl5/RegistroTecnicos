package com.edu.ucne.registrotecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tecnicos")
data class TecnicoEntity(
    @PrimaryKey
    val tecnicoId: Int? = null,
    var nombres: String? = null,
    var sueldoHora: Double? = null,
    var tipoT: String? = null
)
