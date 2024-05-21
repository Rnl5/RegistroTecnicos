package com.edu.ucne.registrotecnicos.presentation.tipoTecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.registrotecnicos.data.local.entities.TipoTecnicoEntity
import com.edu.ucne.registrotecnicos.data.repository.TipoTecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TipoTecnicoViewModel(
    private val repository: TipoTecnicoRepository,
    private val tipoTecnicoId: Int
) :
    ViewModel() {
    var uiState = MutableStateFlow(TipoTecnicoUIState())
        private set

    val tiposTecnicos = repository.getTipoTecnicos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onDescripcionChanged(descripcion: String) {
        uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    init {
        viewModelScope.launch {
            val tipoTecnico = repository.getTipoTecnico(tipoTecnicoId)

            tipoTecnico?.let {
                uiState.update{
                    it.copy(
                        tipoTecnicoId = tipoTecnico.tipoTecnicoId ?: 0,
                        descripcion = tipoTecnico.descripcion.toString()
                    )
                }
            }
        }
    }

    fun saveTipoTecnico() {
        viewModelScope.launch {
            repository.saveTipoTecnico(uiState.value.toEntity())
        }
    }

    suspend fun deleteTipoTecnico(tipoTecnico: TipoTecnicoEntity){
        repository.deleteTipoTecnico(tipoTecnico)
    }
}

data class TipoTecnicoUIState(
    val tipoTecnicoId: Int? = null,
    var descripcion: String = "",
    var descripcionError: String? = null
)

fun TipoTecnicoUIState.toEntity() : TipoTecnicoEntity{
    return TipoTecnicoEntity(
        tipoTecnicoId = tipoTecnicoId,
        descripcion = descripcion
    )
}