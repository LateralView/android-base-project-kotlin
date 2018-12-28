package co.lateralview.myapp.ui.activity.main

import co.lateralview.myapp.domain.exception.permissions.PermissionDeniedException
import co.lateralview.myapp.domain.exception.permissions.PermissionPermanentlyDeniedException
import co.lateralview.myapp.infrastructure.manager.interfaces.LocationManager
import com.google.android.gms.common.api.ResolvableApiException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class MainPresenter(
    private val view: MainContract.View,
    private val locationManager: LocationManager
) : MainContract.Presenter {

    private val disposables = CompositeDisposable()

    override fun init() {
        view.initToolbar(true, view.getToolbarTitle())
    }

    override fun start() {
        locationManager.startLocationUpdates()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.showLocation(it)
            }, { exception ->
                when (exception) {
                    is PermissionDeniedException,
                    is PermissionPermanentlyDeniedException ->
                        // Location permission denied
                        view.finishScreen()
                    is ResolvableApiException ->
                        // Enable location
                        view.showLocationSettingsDialog(exception)
                    else ->
                        Timber.w(exception)
                }
            }).addTo(disposables)
    }

    override fun onToolbarBackPressed() {
        view.finishScreen()
    }

    override fun onLocationEnabled() {
        start()
    }

    override fun onLocationNotEnabled() {
        view.finishScreen()
    }
}
