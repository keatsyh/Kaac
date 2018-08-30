package me.keatsyh.kaac.layout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.keatsyh.kaac.KApp

abstract class KActivity: AppCompatActivity() {
    lateinit var kApp: KApp
    val kaac by lazy {
        kApp.kaac
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kApp = application as KApp
        setContentView(asbLayoutId())
        createViewModel()
    }

    abstract fun createViewModel()

    abstract fun asbLayoutId(): Int

}