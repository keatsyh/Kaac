package me.keatsyh.kaac.config

import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

class DatabaseConfig {

    companion object {
        fun init(): DatabaseConfig {
            return DatabaseConfig()
        }
    }

    var database: RoomDatabase? = null

    inline fun <reified T : RoomDatabase> createDatabase(context: Context, dbName: String): T {
        val database = Room.databaseBuilder(context.applicationContext, T::class.java, "$dbName.db")
                .allowMainThreadQueries()
                .build()
        this.database = database
        return database
    }
}