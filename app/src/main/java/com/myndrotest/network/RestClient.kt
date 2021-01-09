package com.myndrotest.network

import android.content.Intent
import com.google.gson.GsonBuilder
import com.myndrotest.BuildConfig
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

class RestClient {
    fun retrofitCallBack(): Retrofit {
        val logging = HttpLoggingInterceptor()
// set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY

        val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_0)
            .allEnabledCipherSuites()
            .build()

        val client = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
//                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .connectionSpecs(Collections.singletonList(spec)).addInterceptor(Interceptor { chain ->
                var request = chain.request()
                val response = chain.proceed(request)
                response
            }).addInterceptor(logging).build()


        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .baseUrl(BuildConfig.SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    interface NetworkCall {
        @GET("users")
        fun getUserList(@Query("offset") offset: Int, @Query("limit") limit: Int): Call<AppResponse>
    }
}