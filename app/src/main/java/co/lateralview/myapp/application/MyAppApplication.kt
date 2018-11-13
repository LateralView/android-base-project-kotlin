package co.lateralview.myapp.application

import androidx.multidex.MultiDexApplication
import co.lateralview.myapp.BuildConfig
import co.lateralview.myapp.domain.util.CrashlyticsReportingTree
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
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
}