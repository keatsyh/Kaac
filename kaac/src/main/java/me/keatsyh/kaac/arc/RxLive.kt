package me.keatsyh.kaac.arc

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.internal.disposables.DisposableHelper
import io.reactivex.internal.util.EndConsumerHelper
import io.reactivex.subjects.PublishSubject
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.atomic.AtomicReference


class RxLive<T : Any> {


    val lock: Any = Any()

    lateinit var data: T
    lateinit var call: Call<T>
    lateinit var response: Response<T>

    fun send(data: T) {
        synchronized(lock) {
            this.data = data
            notifyChanged()
        }
    }

    fun sendResponse(data: Response<T>) {
        synchronized(lock) {
            this.response = data
            notifyChanged()
        }
    }

    fun get(): T {
        synchronized(lock) {
            return data
        }
    }


    val subject by lazy {
        PublishSubject.create<T>()
    }

    val subjectResponse by lazy {
        PublishSubject.create<Response<T>>()
    }


    fun observe(owner: LifecycleOwner): Observable<T> {
        val dataLifecycle = DataLifecycle<T>(owner)
        val toRxLiveObservable = RxLiveObservable.toRxLiveObservable(dataLifecycle)
        return subject.startWith(data)
                .filter(dataLifecycle.predicateCheck)
                .to(toRxLiveObservable)

    }

    fun observe2(owner: LifecycleOwner): Observable<Response<T>> {
        val dataLifecycle = DataLifecycle<T>(owner)
//        val toRxLiveExecuteObservable = RxLiveExecuteObservable.toRxLiveExecuteObservable(this,call, dataLifecycle)
        return RxLiveExecuteObservable(this, call, dataLifecycle)
    }

    private fun notifyChanged() {
        subject.onNext(data)
    }


    class DataLifecycle<T>(val owner: LifecycleOwner) {

        val predicateCheck: Predicate<T> = Predicate {
            return@Predicate !lifecycleState(owner, Lifecycle.State.DESTROYED)
        }
    }


    class RxLiveObservable<T : Any>(private val observable: Observable<T>, private val dataLifecycle: DataLifecycle<T>) : Observable<T>(), LifecycleObserver {

        companion object {
            fun <T : Any> toRxLiveObservable(dataLifecycle: DataLifecycle<T>): Function<Observable<T>, RxLiveObservable<T>> {
                return Function { observable ->
                    return@Function RxLiveObservable(observable, dataLifecycle)
                }
            }
        }


        init {
            if (!lifecycleState(dataLifecycle.owner, Lifecycle.State.DESTROYED)) {
                dataLifecycle.owner.lifecycle.addObserver(this)
            }
        }


        override fun subscribeActual(observer: Observer<in T>?) {
            val rxLiveObserver: RxLiveObserver<T> = RxLiveObserver(observer)
            observable.subscribe(rxLiveObserver)


            if (lifecycleState(dataLifecycle.owner, Lifecycle.State.DESTROYED)) {
                if (!rxLiveObserver.isDisposed) {
                    rxLiveObserver.dispose()
                }
            }
        }
    }

    class RxLiveExecuteObservable<T : Any>(val rxLive: RxLive<T>, val call: Call<T>, private val dataLifecycle: DataLifecycle<T>) : Observable<Response<T>>(), LifecycleObserver {
        override fun subscribeActual(observer: Observer<in Response<T>>?) {
            val call = call.clone()

            val rxDisposable: RxLiveExecuteDisposable<Response<T>> = RxLiveExecuteDisposable(observer)
//            observable.subscribe(rxLiveObserver)
            observer?.onSubscribe(rxDisposable)

            if (!lifecycleState(dataLifecycle.owner, Lifecycle.State.DESTROYED)) {
                val response = call.execute()
                if (!rxDisposable.isDisposed) {
                    observer?.onNext(response)
//                    rxLive.sendResponse(response)
                }
            } else {
                if (!rxDisposable.isDisposed) {
                    rxDisposable.dispose()
                }
            }
        }

//        companion object {
//            fun <T : Any> toRxLiveExecuteObservable(rxLive: RxLive<T>, call: Call<T>, dataLifecycle: DataLifecycle<T>): Function<Observable<T>, RxLiveExecuteObservable<T>> {
//                return Function {
//                    return@Function RxLiveExecuteObservable(rxLive,call, dataLifecycle)
//                }
//            }
//        }


        init {
            if (!lifecycleState(dataLifecycle.owner, Lifecycle.State.DESTROYED)) {
                dataLifecycle.owner.lifecycle.addObserver(this)
            }
        }

    }

    class RxLiveObserver<T : Any>(private val observer: Observer<in T>?) : Observer<T>, Disposable {


        private val ar: AtomicReference<Disposable> = AtomicReference()


        override fun isDisposed(): Boolean {

            return observer != null && ar.get() == DisposableHelper.DISPOSED
        }

        override fun dispose() {
            DisposableHelper.dispose(ar)
        }


        override fun onComplete() {
            observer?.onComplete()
        }

        override fun onSubscribe(d: Disposable) {
            EndConsumerHelper.setOnce(this.ar, d, javaClass)
            observer?.onSubscribe(d)
        }

        override fun onNext(t: T) {
            observer?.onNext(t)
        }

        override fun onError(e: Throwable) {
            observer?.onError(e)
        }

    }


    class RxLiveExecuteDisposable<T>(private val observer: Observer<in T>?) : Disposable {


        private val ar: AtomicReference<Disposable> = AtomicReference()


        override fun isDisposed(): Boolean {
            return observer != null && ar.get() == DisposableHelper.DISPOSED
        }

        override fun dispose() {
            DisposableHelper.dispose(ar)
        }
    }

}


private fun lifecycleState(owner: LifecycleOwner, state: Lifecycle.State): Boolean {
    return owner.lifecycle.currentState == state
}