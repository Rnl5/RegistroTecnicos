package com.edu.ucne.registrotecnicos.presentation.tecnico

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import com.edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import com.edu.ucne.registrotecnicos.data.repository.TipoTecnicoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TecnicoViewModel(
    private val repository: TecnicoRepository,
    private val tecnicoId: Int,
    private val tipoTecnicoRepository: TipoTecnicoRepository) :
    ViewModel() {

    var uiState = MutableStateFlow(TecnicoUIState())
        private set

    val tecnicos = repository.getTecnicos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val tipoTecnico = tipoTecnicoRepository.getTipoTecnicos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onNombresChanged(nombres: String) {
        uiState.update {
            it.copy(nombres = nombres)
        }
    }

    fun onSueldoHoraChanged(sueldoHora: String){
        uiState.update {
            it.copy(sueldoHora =  sueldoHora.toDouble())
        }
    }

    fun onTipoTecnicoChanged(tipoTecnico: String) {
        uiState.update {
            it.copy(tipoT = tipoTecnico)
        }
    }

    init {
        viewModelScope.launch {
            val tecnico = repository.getTecnico(tecnicoId)

            tecnico?.let {
                uiState.update {
                    it.copy(
                        tecnicoId = tecnico.tecnicoId ?: 0,
                        nombres = tecnico.nombres.toString(),
                        sueldoHora = tecnico.sueldoHora!!,
                        tipoT = tecnico.tipoT
                    )
                }
            }
        }
    }

    fun saveTecnico() {
        viewModelScope.launch {
            repository.saveTecnico(uiState.value.toEntity())
            uiState.value = TecnicoUIState()
        }
    }

    suspend fun deleteTecnico(tecnico: TecnicoEntity) {
        repository.deleteTecnico(tecnico)
    }
}

data class TecnicoUIState(
    val tecnicoId:Int? = null,
    var nombres: String = "",
    var nombresError: String? = null,
    var sueldoHora:Double = 0.0,
    var sueldoHoraError: Double? = null,
    var tipoT: String? = null
)

fun TecnicoUIState.toEntity(): TecnicoEntity{
    return TecnicoEntity(
        tecnicoId = tecnicoId,
        nombres = nombres,
        sueldoHora = sueldoHora,
        tipoT = tipoT
    )
}

