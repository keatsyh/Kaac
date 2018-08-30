package me.keatsyh.kaac

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import me.keatsyh.kaac.arc.KRepository
import me.keatsyh.kaac.arc.KViewModel
import me.keatsyh.kaac.inter.IInject
import me.keatsyh.kaac.inter.IPermission
import timber.log.Timber


class Kaac private constructor(val application: Application) {


    val viewModelFactory by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    companion object {
        @JvmStatic
        fun kaacInit(application: Application): Kaac {
            return Kaac(application)
        }
    }



    fun createVM(instance: Any) {
        val injectorClass = getInjectorClass(instance)
        Timber.d("createVMClass: $injectorClass")
        val injector = Class.forName(injectorClass).newInstance() as IInject
        Timber.d("createVM: $injector")
        injector.inject(instance)
    }

    fun regisPermission(instance: Any){
        val allowClass = getInjectorClass(instance,"PermissionAllow")
        val refuseClass = getInjectorClass(instance,"PermissionRefuse")
        val prohibitClass = getInjectorClass(instance,"PermissionProhibit")
        val rationaleClass = getInjectorClass(instance,"PermissionRationale")

        val allowPermission = Class.forName(allowClass).newInstance() as IPermission
        val refusePermission = Class.forName(refuseClass).newInstance() as IPermission
        val prohibitPermission = Class.forName(prohibitClass).newInstance() as IPermission
        val rationalePermission = Class.forName(rationaleClass).newInstance() as IPermission

        allowPermission.regisPermission(instance)
        refusePermission.regisPermission(instance)
        prohibitPermission.regisPermission(instance)
        rationalePermission.regisPermission(instance)
    }



    private fun getInjectorClass(instance: Any,suffix: String = "BindVM"): String? {
        return "${instance::class.java.`package`.name}.${instance::class.java.simpleName}$suffix"
    }

    fun createRepo(vmInstance: KViewModel<*>) {
        val injectorClass = getInjectorClass(vmInstance,"BindRepo")
        Timber.d("createRepoClass: $injectorClass")
        val injector = Class.forName(injectorClass).newInstance() as IInject
        injector.inject(vmInstance)
    }
}


inline fun <reified A, reified R : KRepository<A>, reified T : KViewModel<R>> Kaac.creatVM(): T {
    val kViewModel = this.viewModelFactory.create(T::class.java)
    val kRepo = kViewModel.createRepo(R::class.java)
    val kApp = this.application as KApp

    kRepo?.let {
        for (kApi in kApp.networkConfig.apiList) {
            if (kApi is A) {
                it.api = kApi
                break
            }
        }

        if(kApp.databaseConfig.database != null) {
            it.rdatabase = kApp.databaseConfig.database
        }
    }

    kViewModel.repoInit(kViewModel.repo)
    return kViewModel
}






