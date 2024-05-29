package com.edu.ucne.registrotecnicos.presentation.servicio_tecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.registrotecnicos.data.local.entities.ServicioTecnicoEntity
import com.edu.ucne.registrotecnicos.data.repository.ServicioTecnicoRepository
import com.edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServicioTecnicoViewModel(
    private val repository: ServicioTecnicoRepository,
    private val servicioTecnicoId: Int,
    private val tecnicoRepository: TecnicoRepository
) : ViewModel() {
    var uiState = MutableStateFlow(ServicioTecnicoUIState())
        private set

    val servicios = repository.getServiciosTecnicos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val tecnico = tecnicoRepository.getTecnicos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onclienteChanged(cliente:String) {
        uiState.update {
            it.copy(cliente = cliente)
        }
    }
    fun onFechaChanged(fecha:String) {
        uiState.update {
            it.copy(fecha = fecha)
        }
    }
    fun onDescripcionChanged(descripcion:String) {
        uiState.update {
            it.copy(descripcion = descripcion)
        }
    }
    fun onTotalChanged(total:String) {
        uiState.update {
            it.copy(total = total.toDouble())
        }
    }

    fun onTecnicoChanged(tecnicoId: Int){
        uiState.update {
            it.copy(tecnicoId = tecnicoId)
        }
    }

    init {
        viewModelScope.launch {
            val servicio = repository.getServicioTecnico(servicioTecnicoId)

            servicio?.let {
                uiState.update {
                    it.copy(
                        servicioTecnicoId = servicio.servicioTecnicoId ?: 0,
                        fecha = servicio.fecha ?: "",
                        tecnicoId = servicio.tecnicoId ?: 0,
                        cliente = servicio.cliente ?: "",
                        descripcion = servicio.descripcion ?: "",
                        total = servicio.total ?: 0.0
                    )
                }
            }
        }
    }

    fun saveServicio(){
        viewModelScope.launch {
            repository.saveServicioTecnico(uiState.value.toEntity())
            uiState.value = ServicioTecnicoUIState()
        }
    }

    suspend fun deleteServicio(servicio: ServicioTecnicoEntity){
        repository.deleteServicioTecnico(servicio)
    }
}



data class ServicioTecnicoUIState(
    val servicioTecnicoId: Int? = null,
    var fecha: String = "",
    var tecnicoId: Int? = null,
    var cliente: String? = null,
    var descripcion: String? = null,
    var total: Double = 0.0,

    var fechaError: String = "",
    var clienteError: String? = null,
    var descripcionError: String? = null,
)

fun ServicioTecnicoUIState.toEntity(): ServicioTecnicoEntity{
    return ServicioTecnicoEntity(
        servicioTecnicoId = servicioTecnicoId,
        fecha = fecha,
        tecnicoId = tecnicoId,
        cliente = cliente,
        descripcion = descripcion,
        total = total
    )
}