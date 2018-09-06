package co.lateralview.myapp.application

import androidx.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen

class MyAppApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
    }
}