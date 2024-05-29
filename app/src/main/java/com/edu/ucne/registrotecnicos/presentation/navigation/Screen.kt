package com.edu.ucne.registrotecnicos.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen() {
    @Serializable
    object TecnicoList : Screen()

    @Serializable
    data class Tecnico(val tecnicoId: Int) : Screen()

    @Serializable
    object TipoTecnicoList : Screen()

    @Serializable
    data class TipoTecnico(val tipoTecnicoId: Int) : Screen()

    @Serializable
    object ServicioList : Screen()

    @Serializable
    data class Servicio(val servicioTecnicoId: Int) : Screen()
}