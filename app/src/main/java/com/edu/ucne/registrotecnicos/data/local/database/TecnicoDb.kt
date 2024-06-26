package com.edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.edu.ucne.registrotecnicos.data.local.dao.ServicioTecnicoDao
import com.edu.ucne.registrotecnicos.data.local.dao.TecnicoDao
import com.edu.ucne.registrotecnicos.data.local.dao.TipoTecnicoDao
import com.edu.ucne.registrotecnicos.data.local.entities.ServicioTecnicoEntity
import com.edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import com.edu.ucne.registrotecnicos.data.local.entities.TipoTecnicoEntity

@Database(
    entities = [
        TecnicoEntity::class,
        TipoTecnicoEntity::class,
        ServicioTecnicoEntity::class
    ],
    version = 5,
    exportSchema = false
)

abstract class TecnicoDb : RoomDatabase(){
    abstract fun tecnicoDao(): TecnicoDao

    abstract fun tipoTecnicoDao(): TipoTecnicoDao

    abstract fun servicioTecnicoDao(): ServicioTecnicoDao
}