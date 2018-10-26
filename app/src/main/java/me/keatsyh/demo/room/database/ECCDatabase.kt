package me.keatsyh.demo.room.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import me.keatsyh.demo.room.dao.BasicDao
import me.keatsyh.demo.BasicData

@Database(entities = (arrayOf(BasicData::class)), version = 1)
abstract class ECCDatabase : RoomDatabase() {
    abstract fun basicDao(): BasicDao

}