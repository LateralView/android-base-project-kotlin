package co.lateralview.myapp.application

import androidx.multidex.MultiDexApplication
import co.lateralview.myapp.BuildConfig
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.perf.FirebasePerformance
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import io.fabric.sdk.android.Fabric

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
}