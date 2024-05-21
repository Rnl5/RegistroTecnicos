package com.edu.ucne.registrotecnicos.data.repository

import com.edu.ucne.registrotecnicos.data.local.dao.TipoTecnicoDao
import com.edu.ucne.registrotecnicos.data.local.entities.TipoTecnicoEntity

class TipoTecnicoRepository(private val tipoTecnicoDao: TipoTecnicoDao) {

    suspend fun saveTipoTecnico(tipoTecnico: TipoTecnicoEntity) = tipoTecnicoDao.save(tipoTecnico)

    fun getTipoTecnicos() = tipoTecnicoDao.getAll()

    suspend fun deleteTipoTecnico(tipoTecnico: TipoTecnicoEntity){
        tipoTecnicoDao.delete(tipoTecnico)
    }

    suspend fun getTipoTecnico(tipoTecnicoId:Int) = tipoTecnicoDao.find(tipoTecnicoId)
}