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
    private var mCustomRetrofit: Retrofit? = null
    private var mCustomCachedRetrofit: Retrofit? = null
    private var mRetrofit: Retrofit? = null
    private var mCustomOkHttpClient: OkHttpClient? = null
    private var mCustomCachedOkHttpClient: OkHttpClient? = null
    private var mDefaultOkHttpClient: OkHttpClient? = null
    private var mCache: Cache? = null

    /**
     * Returns a Custom Retrofit instance.
     */
    // set your desired log level
    val customRetrofit: Retrofit?
        get() {
            if (mCustomRetrofit == null) {
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

                mCustomOkHttpClient = httpClient.build()

                mCustomRetrofit = Retrofit.Builder()
                    .baseUrl(RestConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(mGson))
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                    .client(mCustomOkHttpClient!!)
                    .build()
            }

            return mCustomRetrofit
        }

    /**
     * Returns a Custom Retrofit instance which only checks on Cache.
     */
    // set your desired log level
    val customCachedRetrofit: Retrofit?
        get() {
            if (mCustomCachedRetrofit == null) {
                val logging = HttpLoggingInterceptor()
                logging.level = Level.BODY

                val httpClient = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(provideForcedOfflineCacheInterceptor())
                    .addNetworkInterceptor(StethoInterceptor())
                    .cache(provideCache())
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)

                mCustomCachedOkHttpClient = httpClient.build()

                mCustomCachedRetrofit = Retrofit.Builder()
                    .baseUrl(RestConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(mGson))
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                    .client(mCustomCachedOkHttpClient!!)
                    .build()
            }
            return mCustomCachedRetrofit
        }

    /**
     * Returns a Clean Retrofit instance.
     */
    // set your desired log level
    val retrofit: Retrofit?
        get() {
            if (mRetrofit == null) {
                val logging = HttpLoggingInterceptor()
                logging.level = Level.BODY

                val httpClient = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(provideOfflineCacheInterceptor())
                    .addNetworkInterceptor(StethoInterceptor())
                    .addNetworkInterceptor(provideCacheInterceptor())
                    .cache(provideCache())

                mDefaultOkHttpClient = httpClient.build()

                mRetrofit = Retrofit.Builder()
                    .baseUrl(RestConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(mGson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(mDefaultOkHttpClient!!)
                    .build()
            }
            return mRetrofit
        }

    init {
        Stetho.initializeWithDefaults(mApplication)
    }

    private fun provideCache(): Cache? {
        if (mCache == null) {
            try {
                mCache = Cache(File(mApplication.cacheDir, "http-cache"),
                    (10 * 1024 * 1024).toLong()) // 10 MB
            } catch (e: Exception) {
                Log.e(TAG, "Could not create Cache!")
            }
        }
        return mCache
    }

    private fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())

            val isOnline = mInternetManager.isOnline ?: false
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

            val isOnline = mInternetManager.isOnline ?: false
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

        if (mCustomOkHttpClient != null) {
            // Cancel Pending Request
            mCustomOkHttpClient!!.dispatcher().cancelAll()
        }

        if (mDefaultOkHttpClient != null) {
            // Cancel Pending Request
            mDefaultOkHttpClient!!.dispatcher().cancelAll()
        }

        mCustomRetrofit = null
        mRetrofit = null

        if (mCache != null) {
            try {
                mCache!!.evictAll()
            } catch (e: IOException) {
                Log.e(TAG, "Error cleaning http cache")
            }
        }
        mCache = null
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