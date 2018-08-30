package me.keatsyh.kaac.arc

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import java.lang.reflect.InvocationTargetException

abstract class KViewModel<R : KRepository<*>>(application: Application) : AndroidViewModel(application) {

    lateinit var repo: R

    fun createRepo(repoClass: Class<R>): R? {
        if (KRepository::class.java.isAssignableFrom(repoClass)) return try {
            repo = repoClass.getConstructor().newInstance()
            repo
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Cannot create an instance of $repoClass", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Cannot create an instance of $repoClass", e)
        } catch (e: InstantiationException) {
            throw RuntimeException("Cannot create an instance of $repoClass", e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Cannot create an instance of $repoClass", e)
        }
        return null
    }

    abstract fun repoInit(repo: R)


}