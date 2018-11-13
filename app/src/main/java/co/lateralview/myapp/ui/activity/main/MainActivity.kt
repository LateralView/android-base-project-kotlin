package co.lateralview.myapp.ui.activity.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import co.lateralview.myapp.R
import co.lateralview.myapp.ui.activity.base.BaseActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View {

    companion object {
        const val LOCATION_SETTINGS_REQUEST_CODE = 1
    }

    @Inject lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.init()

        startButton.setOnClickListener { presenter.start() }
    }

    override fun initToolbar(backEnabled: Boolean, toolbarTitle: String) {
        initializeToolbar(backEnabled, toolbarTitle)
    }

    override fun getToolbarTitle(): String = getString(R.string.toolbar_title)

    override fun onToolbarBackPressed() {
        presenter.onToolbarBackPressed()
    }

    override fun finishScreen() {
        finish()
    }

    override fun showLocation(location: LatLng) {
        locationTextView.text = location.toString()
    }

    override fun showLocationSettingsDialog(exception: ResolvableApiException) {
        exception.startResolutionForResult(this, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOCATION_SETTINGS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    presenter.onLocationEnabled()
                } else {
                    presenter.onLocationNotEnabled()
                }
            }
        }
    }
}
