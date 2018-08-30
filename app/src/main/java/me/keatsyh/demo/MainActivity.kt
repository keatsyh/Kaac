package me.keatsyh.demo

import android.os.Bundle
import me.keatsyh.kaac.creatVM
import me.keatsyh.kaac.layout.KActivity
import timber.log.Timber

class MainActivity : KActivity () {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val creatVM = kaac.creatVM<MyViewModel>()
        val densityDpi = resources.displayMetrics.densityDpi
        Timber.d("densityDpi: $densityDpi")
    }
}
