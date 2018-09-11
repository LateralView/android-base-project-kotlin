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

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }

        LeakCanary.install(this)
        AndroidThreeTen.init(this)
        initializeFirebase()
        initializeTimber()
        initializeStetho()
    }

    private fun initializeStetho() {
        Stetho.initializeWithDefaults(this)
    }

    private fun initializeFirebase() {
        // Initialize Firebase Crashlytics
        if (BuildConfig.CRASHLYTICS_ENABLED) {
            Fabric.with(this, Crashlytics())
        }

        // Initialize Firebase Performance Monitoring
        FirebasePerformance.getInstance().isPerformanceCollectionEnabled = BuildConfig.PERFORMANCE_MONITORING_ENABLED

        // Initialize Firebase Analytics
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(BuildConfig.ANALYTICS_ENABLED)
    }

    private fun initializeTimber() {
        if (BuildConfig.LOGCAT_ENABLED) {
            Timber.plant(Timber.DebugTree())
        }

        if (BuildConfig.CRASHLYTICS_ENABLED) {
            Timber.plant(CrashlyticsReportingTree())
        }
    }
}