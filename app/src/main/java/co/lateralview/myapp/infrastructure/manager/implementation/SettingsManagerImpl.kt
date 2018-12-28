package co.lateralview.myapp.infrastructure.manager.implementation

import co.lateralview.myapp.infrastructure.manager.interfaces.SettingsManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import io.reactivex.Completable
import timber.log.Timber

class SettingsManagerImpl(private val settingsClient: SettingsClient) : SettingsManager {

    override fun checkLocationSettings(locationRequest: LocationRequest): Completable {
        return Completable.create { emitter ->
            val settingsBuilder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true)

            settingsClient.checkLocationSettings(settingsBuilder.build())
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    Timber.w(it)
                    emitter.onError(it)
                }
        }
    }
}