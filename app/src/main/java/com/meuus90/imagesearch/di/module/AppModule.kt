package com.meuus90.imagesearch.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.meuus90.imagesearch.BuildConfig
import com.meuus90.imagesearch.base.arch.util.livedata.LiveDataCallAdapterFactory
import com.meuus90.imagesearch.base.arch.util.network.entity.NetworkError
import com.meuus90.imagesearch.model.data.source.api.DaumAPI
import com.meuus90.imagesearch.model.data.source.local.Cache
import com.orhanobut.logger.Logger
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * A module for Android-specific dependencies which require a [Context] or
 * [android.app.Application] to create.
 */
@Module
class AppModule {
    companion object {
        const val timeout_read = 30L
        const val timeout_connect = 30L
        const val timeout_write = 30L
    }

    @Provides
    @Singleton
    fun appContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val ok = OkHttpClient.Builder()
            .connectTimeout(timeout_connect, TimeUnit.SECONDS)
            .readTimeout(timeout_read, TimeUnit.SECONDS)
            .writeTimeout(timeout_write, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    if (isJSONValid(message))
                        Logger.json(message)
                    else
                        Timber.d("pretty $message")
                }

                fun isJSONValid(jsonInString: String): Boolean {
                    try {
                        JSONObject(jsonInString)
                    } catch (ex: JSONException) {
                        try {
                            JSONArray(jsonInString)
                        } catch (ex1: JSONException) {
                            return false
                        }
                    }

                    return true
                }

            })

            logging.level = HttpLoggingInterceptor.Level.BODY
            ok.addInterceptor(logging)
        }

        ok.addInterceptor(interceptor)
        return ok.build()
    }

    @Provides
    @Singleton
    fun provideGSon(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    //@Singleton
    fun getRequestInterceptor(): Interceptor {
        return Interceptor {
            Timber.tag("NETWORK_LOGGER")

            val original = it.request()

            val requested = with(original) {
                val builder = newBuilder()

                builder.header("Accept", "application/json")
                    .header("Authorization", "KakaoAK ${BuildConfig.daumApiKey}")
                    .build()
            }

            val response = it.proceed(requested)
            val body = response.body
            val bodyStr = body?.string()
            Timber.e("**http-num: ${response.code}")
            Timber.e("**http-body: $body")

            val cookies = HashSet<String>()
            for (header in response.headers("Set-Cookie")) {
                cookies.add(header)
            }

            if (!response.isSuccessful) {
                try {
                    val networkError = Gson().fromJson(bodyStr, NetworkError::class.java)

                    networkError.errorType?.let {
                        //do something on error
                    }
                } catch (e: Exception) {

                }
            }

            val builder = response.newBuilder()

            builder.body(
                ResponseBody.create(
                    body?.contentType(), bodyStr?.toByteArray()!!
                )
            ).build()

        }
    }

    @Singleton
    @Provides
    fun provideDaumAPI(gson: Gson, okHttpClient: OkHttpClient): DaumAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.daumApiServer)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(okHttpClient)
            .build()

        return retrofit.create(DaumAPI::class.java)
    }

    @Singleton
    @Provides
    internal fun provideCache(app: Application) =
        Room.databaseBuilder(app, Cache::class.java, "daum_search.db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    internal fun provideImageModelDao(cache: Cache) = cache.imageDao()
}