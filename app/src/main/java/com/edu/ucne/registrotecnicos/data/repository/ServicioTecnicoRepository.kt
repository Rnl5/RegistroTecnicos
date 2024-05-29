package com.edu.ucne.registrotecnicos.data.repository

import com.edu.ucne.registrotecnicos.data.local.dao.ServicioTecnicoDao
import com.edu.ucne.registrotecnicos.data.local.entities.ServicioTecnicoEntity

class ServicioTecnicoRepository(private val servicioTecnicoDao: ServicioTecnicoDao) {
    suspend fun saveServicioTecnico(servicio: ServicioTecnicoEntity) = servicioTecnicoDao.save(servicio)


    fun getServiciosTecnicos() = servicioTecnicoDao.getAll()

    suspend fun deleteServicioTecnico(servicio: ServicioTecnicoEntity){
        servicioTecnicoDao.delete(servicio)
    }

    suspend fun getServicioTecnico(servicioTecnicoId:Int) = servicioTecnicoDao.find(servicioTecnicoId)
}