package me.keatsyh.kaac.arc

import android.arch.persistence.room.RoomDatabase
import me.keatsyh.kaac.KApp
import me.keatsyh.kaac.extended.getApp
import me.keatsyh.kaac.extended.getKaac


open class KRepository<A : Any> {

    lateinit var api: A
    var rdatabase: RoomDatabase? = null

//    fun <T: RoomDatabase> getDatabase(): T? {
//        if (rdatabase != null) {
//            return rdatabase as T
//        }
//        return null
//    }

    fun <T : RoomDatabase> getDatabase(): T? {
        val database = getApp<KApp>().databaseConfig.database
        return database?.let {
            it as T
        }
    }


    inline fun <reified T> getApiServer(): T? {
        val apiList = getApp<KApp>().networkConfig.apiList
        apiList.forEach {
            if (it is T) {
                return it
            }
        }
        return null
    }

}