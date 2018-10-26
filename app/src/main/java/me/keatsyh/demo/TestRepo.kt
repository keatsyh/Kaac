package me.keatsyh.demo

import me.keatsyh.demo.room.database.ECCDatabase
import me.keatsyh.kaac.arc.KRepository
import timber.log.Timber

class TestRepo: KRepository<Any>() {

    init {
        val apiServer = getApiServer<ClassCardApi>()
        val database = getDatabase<ECCDatabase>()
    }

}