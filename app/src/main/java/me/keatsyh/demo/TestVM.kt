package me.keatsyh.demo

import android.app.Application
import me.keatsyh.annotation.BindRepo

import me.keatsyh.kaac.arc.KRepository
import me.keatsyh.kaac.arc.KViewModel
import me.keatsyh.kaac.extended.getKaac
import timber.log.Timber

class TestVM(application: Application): KViewModel<KRepository<Any>>(application) {
    override fun repoInit(repo: KRepository<Any>) {
    }

    @BindRepo("TestRepo")
    lateinit var testRepo: TestRepo

    init {
        getKaac().createRepo(this)
        Timber.d("TestRepo:  $testRepo")
    }
}