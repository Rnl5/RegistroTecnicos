package com.edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.edu.ucne.registrotecnicos.data.local.dao.TecnicoDao
import com.edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity

@Database(
    entities = [
        TecnicoEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class TecnicoDb : RoomDatabase(){
    abstract fun tecnicoDao(): TecnicoDao
}