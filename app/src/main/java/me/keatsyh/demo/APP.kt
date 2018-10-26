package me.keatsyh.demo

import me.keatsyh.demo.room.database.ECCDatabase
import me.keatsyh.kaac.KApp


class APP : KApp() {

    var classCardApi: ClassCardApi? = null
    override fun appConfig() {

        classCardApi =  networkConfig.Builder(networkConfig)
                .addBaseUrl("http://192.168.0.53:8080/")
                .register()

//        val createDatabase = databaseConfig.createDatabase<ECCDatabase>(this, "demo")
    }


}