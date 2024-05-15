package com.edu.ucne.registrotecnicos.data.repository

import com.edu.ucne.registrotecnicos.data.local.dao.TecnicoDao
import com.edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity

class TecnicoRepository(private val tecnicoDao: TecnicoDao) {
    suspend fun saveTecnico(tecnico: TecnicoEntity) = tecnicoDao.save(tecnico)

    fun getTecnicos() = tecnicoDao.getAll()

    suspend fun deleteTecnico(tecnico: TecnicoEntity){
        tecnicoDao.delete(tecnico)
    }

}