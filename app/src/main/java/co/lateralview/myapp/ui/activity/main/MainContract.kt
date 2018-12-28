package co.lateralview.myapp.ui.activity.main

import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.model.LatLng

interface MainContract {
    interface View {
        fun initToolbar(backEnabled: Boolean, toolbarTitle: String)
        fun getToolbarTitle(): String
        fun finishScreen()
        fun showLocationSettingsDialog(exception: ResolvableApiException)
        fun showLocation(location: LatLng)
    }

    interface Presenter {
        fun init()
        fun onToolbarBackPressed()
        fun start()
        fun onLocationEnabled()
        fun onLocationNotEnabled()
    }
}
