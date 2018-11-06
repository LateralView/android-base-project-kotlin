package co.lateralview.myapp.infrastructure.manager.interfaces

import com.google.android.gms.location.LocationRequest
import io.reactivex.Completable

interface SettingsManager {
    fun checkLocationSettings(locationRequest: LocationRequest): Completable
}