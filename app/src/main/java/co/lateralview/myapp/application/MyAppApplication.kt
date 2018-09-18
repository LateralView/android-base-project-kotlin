package co.lateralview.myapp.application

import androidx.multidex.MultiDexApplication
import co.lateralview.myapp.BuildConfig
import co.lateralview.myapp.domain.util.CrashlyticsReportingTree
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.perf.FirebasePerformance
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import io.fabric.sdk.android.Fabric
import timber.log.Timber

class MyAppApplication : MultiDexApplication() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }

        appComponent.inject(this)
        LeakCanary.install(this)
        AndroidThreeTen.init(this)
        initializeAnalytics()
        initializeCrashReporting()
        initializePerformanceMonitoring()
        initializeLogs()
    }

    private fun initializeAnalytics() {
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(BuildConfig.ANALYTICS_ENABLED)
    }

    private fun initializeCrashReporting() {
        // Firebase Crashlytics
        if (BuildConfig.CRASHLYTICS_ENABLED) {
            Fabric.with(this, Crashlytics())
        }
    }

    private fun initializePerformanceMonitoring() {
        FirebasePerformance.getInstance().isPerformanceCollectionEnabled = BuildConfig.PERFORMANCE_MONITORING_ENABLED
    }

    private fun initializeLogs() {
        initializeTimber()
        initializeStetho()
    }

    private fun initializeTimber() {
        if (BuildConfig.LOGS_ENABLED) {
            // Show logs in logcat
            Timber.plant(Timber.DebugTree())
        }

        if (BuildConfig.CRASHLYTICS_ENABLED) {
            // Show logs and crashes in crashlytics
            Timber.plant(CrashlyticsReportingTree())
        }
    }

    private fun initializeStetho() {
        if (BuildConfig.LOGS_ENABLED) {
            Stetho.initializeWithDefaults(this)
        }
    }
}