package me.keatsyh.kaac.config

import android.util.Log
import me.keatsyh.RxLiveCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import retrofit2.converter.gson.GsonConverterFactory


class NetworkConfig {

    val apiList: MutableList<Any> = ArrayList()

    companion object {
        fun init(): NetworkConfig {
            return NetworkConfig()
        }
    }


    fun configOkHttp(): OkHttpClient.Builder {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)

    }

    fun configRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addCallAdapterFactory(RxLiveCallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
    }


    inner class Builder(val networkConfig: NetworkConfig) {
        val okBuilder: OkHttpClient.Builder
        val reBuilder: Retrofit.Builder
        init {
            Log.d("NetworkConfig","$networkConfig")
            okBuilder = networkConfig.configOkHttp()
            reBuilder = networkConfig.configRetrofit()
            Log.d("NetworkConfig","$networkConfig")
        }


        fun addInterceptor(interceptor: Interceptor): Builder {
            okBuilder.addInterceptor(interceptor)
            return this
        }

        fun addNetworkInterceptor(interceptor: Interceptor): Builder {
            okBuilder.addNetworkInterceptor(interceptor)
            return this
        }

        fun addConverterFactory(converter: Converter.Factory): Builder {
            reBuilder.addConverterFactory(converter)
            return this
        }

        fun addCallAdapterFactory(callAdapter: CallAdapter.Factory): Builder {
            reBuilder.addCallAdapterFactory(callAdapter)
            return this
        }

        fun addBaseUrl(baseUrl: String): Builder {
            reBuilder.baseUrl(baseUrl)
            return this
        }

        inline fun <reified T : Any> register(): T {
            val okClient = okBuilder.build()
            reBuilder.client(okClient)
            val reClient = reBuilder.build()
            val apiService = reClient.create(T::class.java)
            networkConfig.apiList.add(apiService)
            return apiService
        }
    }


//
//    val okBuilder: OkHttpClient.Builder = configOkHttp()
//    val reBuilder: Retrofit.Builder = configRetrofit()
//
//    fun addInterceptor(interceptor: Interceptor): NetworkConfig {
//        okBuilder.addInterceptor(interceptor)
//        return this
//    }
//
//    fun addNetworkInterceptor(interceptor: Interceptor): NetworkConfig {
//        okBuilder.addNetworkInterceptor(interceptor)
//        return this
//    }
//
//    fun addBaseUrl(baseUrl: String): NetworkConfig {
//        Log.d("ECCAPP","${this@NetworkConfig}  $reBuilder")
//        reBuilder.baseUrl(baseUrl)
//        return this
//    }
//
//    inline fun <reified T:Any> register(): T {
//        val okClient = okBuilder.build()
//        reBuilder.client(okClient)
//        val reClient = reBuilder.build()
//        val apiService = reClient.create(T::class.java)
//        apiList.add(apiService)
//        return apiService
//    }

}


