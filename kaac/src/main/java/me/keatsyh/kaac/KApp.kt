package me.keatsyh.kaac

import android.app.Application
import me.keatsyh.kaac.config.DatabaseConfig
import me.keatsyh.kaac.config.NetworkConfig
import me.keatsyh.kaac.config.TimberConfig

abstract class KApp : Application() {


    val kaac: Kaac by lazy {
        Kaac.kaacInit(this)
    }


    lateinit var networkConfig: NetworkConfig
    lateinit var timberConfig: TimberConfig
    lateinit var databaseConfig: DatabaseConfig

    init {
        application = this
    }

    companion object {
        private lateinit var application: Application
        fun <T : Application> getApp(): T {
            return application as T
        }
    }


    override fun onCreate() {
        super.onCreate()
        // 日志初始化
        timberConfig = TimberConfig.init()
        // 网络初始化
        networkConfig = NetworkConfig.init()
        // 数据库初始化
        databaseConfig = DatabaseConfig.init()
        appConfig()
    }

    abstract fun appConfig()


}
