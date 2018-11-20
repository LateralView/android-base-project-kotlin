package co.lateralview.myapp.application

import android.content.Context
import androidx.multidex.MultiDex
import co.lateralview.myapp.BuildConfig
import co.lateralview.myapp.domain.util.CrashlyticsReportingTree
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class MyAppApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }

        LeakCanary.install(this)
        AndroidThreeTen.init(this)
        initializeLogs()
    }

    private fun initializeLogs() {
        initializeTimber()
        initializeStetho()
    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) {
            // Show logs in logcat
            Timber.plant(Timber.DebugTree())
        }

        if (BuildConfig.LOG_TO_CRASHLYTICS) {
            // Show logs and crashes in crashlytics
            Timber.plant(CrashlyticsReportingTree())
        }
    }

    private fun initializeStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}