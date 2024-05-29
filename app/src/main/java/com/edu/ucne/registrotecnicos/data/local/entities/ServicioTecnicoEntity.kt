package com.edu.ucne.registrotecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ServiciosTecnicos")
data class ServicioTecnicoEntity(
    @PrimaryKey
    val servicioTecnicoId:Int? = null,
    var fecha: String = "",
    var tecnicoId: Int? = null,
    var cliente: String? = null,
    var descripcion: String? = null,
    var total: Double = 0.0
)
