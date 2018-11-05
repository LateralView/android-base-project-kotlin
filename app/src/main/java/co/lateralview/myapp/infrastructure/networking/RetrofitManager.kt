package co.lateralview.myapp.infrastructure.networking

import android.app.Application
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
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitManager(
    private val application: Application,
    private val gson: Gson,
    private val sessionRepository: SessionRepository,
    private val internetManager: InternetManager
) {
    private var customOkHttpClient: OkHttpClient? = null
    private var customCachedOkHttpClient: OkHttpClient? = null
    private var defaultOkHttpClient: OkHttpClient? = null

    companion object {
        const val HEADER_CACHE_CONTROL = "Cache-Control"
        const val HEADER_PRAGMA = "Pragma"
    }

    init {
        Stetho.initializeWithDefaults(application)
    }

    /**
     * Returns a Custom Retrofit instance.
     */
    // set your desired log level
    val customRetrofit: Retrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = Level.BODY

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(provideHeaderInterceptor())
            .addInterceptor(provideOfflineCacheInterceptor())
            .addNetworkInterceptor(ChuckInterceptor(application))
            .addNetworkInterceptor(StethoInterceptor())
            .addNetworkInterceptor(provideCacheInterceptor())
            .cache(cache)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)

        customOkHttpClient = httpClient.build()

        Retrofit.Builder()
            .baseUrl(RestConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
            .client(customOkHttpClient!!)
            .build()
    }

    /**
     * Returns a Custom Retrofit instance which only checks on Cache.
     */
    // set your desired log level
    val customCachedRetrofit: Retrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = Level.BODY

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(provideForcedOfflineCacheInterceptor())
            .addNetworkInterceptor(StethoInterceptor())
            .cache(cache)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)

        customCachedOkHttpClient = httpClient.build()

        Retrofit.Builder()
            .baseUrl(RestConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
            .client(customCachedOkHttpClient!!)
            .build()
    }

    /**
     * Returns a Clean Retrofit instance.
     */
    // set your desired log level
    val retrofit: Retrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = Level.BODY

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(provideOfflineCacheInterceptor())
            .addNetworkInterceptor(StethoInterceptor())
            .addNetworkInterceptor(provideCacheInterceptor())
            .cache(cache)

        defaultOkHttpClient = httpClient.build()

        Retrofit.Builder()
            .baseUrl(RestConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(defaultOkHttpClient!!)
            .build()
    }

    private val cache: Cache by lazy {
        Cache(File(application.cacheDir, "http-cache"), (10 * 1024 * 1024).toLong())
    }

    private fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())

            val isOnline = internetManager.isOnline ?: false
            val cacheControl: CacheControl = if (isOnline) {
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

            val isOnline = internetManager.isOnline ?: false
            if (!isOnline) {
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
        if (customOkHttpClient != null) {
            // Cancel Pending Request
            customOkHttpClient!!.dispatcher().cancelAll()
        }

        if (customCachedOkHttpClient != null) {
            // Cancel Pending Request
            customCachedOkHttpClient!!.dispatcher().cancelAll()
        }

        if (defaultOkHttpClient != null) {
            // Cancel Pending Request
            defaultOkHttpClient!!.dispatcher().cancelAll()
        }

        try {
            cache.evictAll()
        } catch (e: IOException) {
            Timber.e("Error cleaning http cache")
        }
    }

    private fun provideHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()

            val accessToken = sessionRepository.getAccessToken().onErrorReturnItem("").blockingGet()

            val newRequest = if (!accessToken.isNullOrEmpty()) {
                request.newBuilder()
                    .addHeader(RestConstants.HEADER_AUTH, accessToken)
                    .build()
            } else {
                request
            }

            chain.proceed(newRequest)
        }
    }
}