package com.edu.ucne.registrotecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TipoTecnicos")
data class TipoTecnicoEntity(
    @PrimaryKey
    val tipoTecnicoId: Int? = null,
    var descripcion: String? = null
)
