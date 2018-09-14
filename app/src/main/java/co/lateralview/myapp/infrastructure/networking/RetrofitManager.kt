package co.lateralview.myapp.infrastructure.networking

import android.app.Application
import android.util.Log
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository
import co.lateralview.myapp.infrastructure.manager.InternetManager
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitManager(
    private val mApplication: Application,
    private val mGson: Gson,
    private val mSessionRepository: SessionRepository,
    private val mInternetManager: InternetManager
) {
    private var globalCustomRetrofit: Retrofit? = null
    private var globalCustomCachedRetrofit: Retrofit? = null
    private var globalRetrofit: Retrofit? = null
    private var globalCustomOkHttpClient: OkHttpClient? = null
    private var globalCustomCachedOkHttpClient: OkHttpClient? = null
    private var globalDefaultOkHttpClient: OkHttpClient? = null
    private var globalCache: Cache? = null

    /**
     * Returns a Custom Retrofit instance.
     */
    // set your desired log level
    val customRetrofit: Retrofit?
        get() {
            if (globalCustomRetrofit == null) {
                val logging = HttpLoggingInterceptor()
                logging.level = Level.BODY

                val httpClient = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(provideHeaderInterceptor())
                    .addInterceptor(provideOfflineCacheInterceptor())
                    .addInterceptor(ChuckInterceptor(mApplication))
                    .addNetworkInterceptor(StethoInterceptor())
                    .addNetworkInterceptor(provideCacheInterceptor())
                    .cache(provideCache())
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)

                globalCustomOkHttpClient = httpClient.build()

                globalCustomRetrofit = Retrofit.Builder()
                    .baseUrl(RestConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(mGson))
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                    .client(globalCustomOkHttpClient!!)
                    .build()
            }

            return globalCustomRetrofit
        }

    /**
     * Returns a Custom Retrofit instance which only checks on Cache.
     */
    // set your desired log level
    val customCachedRetrofit: Retrofit?
        get() {
            if (globalCustomCachedRetrofit == null) {
                val logging = HttpLoggingInterceptor()
                logging.level = Level.BODY

                val httpClient = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(provideForcedOfflineCacheInterceptor())
                    .addNetworkInterceptor(StethoInterceptor())
                    .cache(provideCache())
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)

                globalCustomCachedOkHttpClient = httpClient.build()

                globalCustomCachedRetrofit = Retrofit.Builder()
                    .baseUrl(RestConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(mGson))
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                    .client(globalCustomCachedOkHttpClient!!)
                    .build()
            }
            return globalCustomCachedRetrofit
        }

    /**
     * Returns a Clean Retrofit instance.
     */
    // set your desired log level
    val retrofit: Retrofit?
        get() {
            if (globalRetrofit == null) {
                val logging = HttpLoggingInterceptor()
                logging.level = Level.BODY

                val httpClient = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(provideOfflineCacheInterceptor())
                    .addNetworkInterceptor(StethoInterceptor())
                    .addNetworkInterceptor(provideCacheInterceptor())
                    .cache(provideCache())

                globalDefaultOkHttpClient = httpClient.build()

                globalRetrofit = Retrofit.Builder()
                    .baseUrl(RestConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(mGson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(globalDefaultOkHttpClient!!)
                    .build()
            }
            return globalRetrofit
        }

    init {
        Stetho.initializeWithDefaults(mApplication)
    }

    private fun provideCache(): Cache? {
        if (globalCache == null) {
            try {
                globalCache = Cache(File(mApplication.cacheDir, "http-cache"),
                    (10 * 1024 * 1024).toLong()) // 10 MB
            } catch (e: Exception) {
                Log.e(TAG, "Could not create Cache!")
            }
        }
        return globalCache
    }

    private fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())

            val localInternalManagerIsOnline = mInternetManager.isOnline ?: false
            val cacheControl: CacheControl = if (localInternalManagerIsOnline) {
                CacheControl.Builder()
                    .maxAge(0, TimeUnit.SECONDS)
                    .build()
            } else {
                CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()
            }

            response.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }

    private fun provideOfflineCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()

            val localInternalManagerIsOnline = mInternetManager.isOnline ?: false
            if ((!localInternalManagerIsOnline)) {
                val cacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()

                request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build()
            }

            chain.proceed(request)
        }
    }

    private fun provideForcedOfflineCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()

            val cacheControl = CacheControl.Builder()
                .maxStale(7, TimeUnit.DAYS)
                .build()

            request = request.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .cacheControl(cacheControl)
                .build()

            chain.proceed(request)
        }
    }

    fun clean() {

        if (globalCustomOkHttpClient != null) {
            // Cancel Pending Request
            globalCustomOkHttpClient!!.dispatcher().cancelAll()
        }

        if (globalDefaultOkHttpClient != null) {
            // Cancel Pending Request
            globalDefaultOkHttpClient!!.dispatcher().cancelAll()
        }

        globalCustomRetrofit = null
        globalRetrofit = null

        if (globalCache != null) {
            try {
                globalCache!!.evictAll()
            } catch (e: IOException) {
                Log.e(TAG, "Error cleaning http cache")
            }
        }
        globalCache = null
    }

    private fun provideHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()

            val accessToken = mSessionRepository.getAccessToken()!!.blockingGet()

            val requestBuilder = request.newBuilder()
                .header(RestConstants.HEADER_AUTH,
                    accessToken ?: "")

            request = requestBuilder.build()

            chain.proceed(request)
        }
    }

    companion object {
        const val TAG = "RetrofitManager"
        const val HEADER_CACHE_CONTROL = "Cache-Control"
        const val HEADER_PRAGMA = "Pragma"
    }
}