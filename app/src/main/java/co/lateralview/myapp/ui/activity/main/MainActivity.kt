package co.lateralview.myapp.ui.activity.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.lateralview.myapp.R
import co.lateralview.myapp.application.MyAppApplication

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependencies()
    }

    private fun injectDependencies() {
        (application as MyAppApplication).appComponent
            .plus(MainModule(this))
            .inject(this)
    }
}
