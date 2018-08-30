package me.keatsyh

import java.lang.reflect.Type

import me.keatsyh.kaac.arc.RxLive
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import java.lang.reflect.ParameterizedType


class RxLiveCallAdapter<R:Any>(val responseType: Type) : CallAdapter<R, Any> {



    override fun responseType(): Type? {

        return responseType
    }


    override fun adapt(call: Call<R>?): Any {

        val rxLive = RxLive<R>()
        rxLive.call = call!!
        return rxLive

    }


}


class RxLiveCallAdapterFactory : CallAdapter.Factory() {

    companion object {
        fun create():RxLiveCallAdapterFactory {
            return RxLiveCallAdapterFactory()
        }
    }


    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *>? {
        return returnType?.let {
            val rawType = getRawType(returnType)
            val observableType = CallAdapter.Factory.getParameterUpperBound(0, returnType as ParameterizedType)
            Timber.d("rawType: $rawType returnType: $returnType   observableType： $observableType")
//            RxLiveCallAdapter()
            RxLiveCallAdapter<Any>(observableType)
        }
    }

}
