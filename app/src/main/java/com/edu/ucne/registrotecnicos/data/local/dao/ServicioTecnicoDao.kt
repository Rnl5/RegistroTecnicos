package com.edu.ucne.registrotecnicos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.edu.ucne.registrotecnicos.data.local.entities.ServicioTecnicoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServicioTecnicoDao {
    @Upsert()
    suspend fun save(servicio: ServicioTecnicoEntity)

    @Query(
        """
        SELECT *
            FROM ServiciosTecnicos
            WHERE servicioTecnicoId=:id
            LIMIT 1
        """
    )


    suspend fun find(id:Int): ServicioTecnicoEntity?

    @Delete
    suspend fun delete(servicio: ServicioTecnicoEntity)

    @Query("SELECT * FROM ServiciosTecnicos")
    fun getAll(): Flow<List<ServicioTecnicoEntity>>
}