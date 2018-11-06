package co.lateralview.myapp.infrastructure.manager.implementation

import android.annotation.SuppressLint
import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import co.lateralview.myapp.domain.exception.networking.NetworkException
import co.lateralview.myapp.infrastructure.manager.interfaces.LocationManager
import co.lateralview.myapp.infrastructure.manager.interfaces.PermissionsManager
import co.lateralview.myapp.infrastructure.manager.interfaces.SettingsManager
import co.lateralview.myapp.ui.util.Configuration
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.Locale

class LocationManagerImpl(
    private val locationClient: FusedLocationProviderClient,
    private val settingsManager: SettingsManager,
    private val permissionsManager: PermissionsManager,
    private val context: Application
) : LocationManager {

    companion object {
        val EMPTY_LOCATION = LatLng(0.0, 0.0)
    }

    private val locationRequest = LocationRequest().apply {
        interval = Configuration.LOCATION_UPDATES_INTERVAL
        fastestInterval = Configuration.LOCATION_UPDATES_FASTEST_INTERVAL
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun getLastLocation(): Single<LatLng> {
        return startLocationUpdates().take(1).singleOrError()
    }

    /**
     * Does not throw errors, it returns an empty location instead.
     */
    override fun getLastLocationOrEmpty(): Single<LatLng> {
        return startLocationUpdates().take(1).singleOrError().onErrorReturn { EMPTY_LOCATION }
    }

    override fun startLocationUpdates(): Observable<LatLng> {
        return permissionsManager.checkLocationPermission(context)
            .andThen(settingsManager.checkLocationSettings(locationRequest))
            .andThen(requestLocationUpdates())
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates(): Observable<LatLng> {
        return Observable.create<LatLng> { emitter ->
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    val lastLocation = locationResult?.lastLocation
                    if (lastLocation == null) {
                        Timber.w("Null Location")
                        emitter.onError(Throwable("Last Location is null"))
                        return
                    }

                    Timber.d("Last location - lat:${lastLocation.latitude} lng: ${lastLocation.longitude}")
                    if (!Configuration.IS_MOCK_LOCATION_ALLOWED && lastLocation.isFromMockProvider) {
                        Timber.w("Mock location not allowed")
                        emitter.onError(Throwable("Mock location not allowed"))
                        return
                    }

                    emitter.onNext(LatLng(lastLocation.latitude, lastLocation.longitude))
                }
            }

            val locationCallbackRef = LocationCallbackReference(locationCallback)
            locationClient.requestLocationUpdates(locationRequest, locationCallbackRef, null)
            emitter.setCancellable { locationClient.removeLocationUpdates(locationCallbackRef) }
        }
    }

    override fun distanceBetween(startLatLng: LatLng, endLatLng: LatLng, unit: LocationManager.DistanceUnit): Double {
        val distanceInMeters = FloatArray(1)
        Location.distanceBetween(startLatLng.latitude,
            startLatLng.longitude,
            endLatLng.latitude,
            endLatLng.longitude,
            distanceInMeters)

        return distanceInMeters[0] * unit.multiplier
    }

    override fun getLocationFromName(locationName: String): Single<LatLng> {
        return Single.create<LatLng> { emitter ->
            val geocoder = Geocoder(context, Locale.getDefault())
            var locations: List<Address> = emptyList()
            try {
                locations = geocoder.getFromLocationName(locationName, 1)
            } catch (ioException: IOException) {
                emitter.onError(NetworkException())
            }

            if (locations.isNotEmpty() && locations[0].hasLatitude() && locations[0].hasLongitude()) {
                emitter.onSuccess(LatLng(locations[0].latitude, locations[0].longitude))
            } else {
                emitter.onError(Exception("Location not found"))
            }
        }.subscribeOn(Schedulers.io())
    }

    /**
     * Location API has a memory leak, use a WeakReference to avoid that.
     * <p>
     * val locationCallbackRef = LocationCallbackReference(locationCallback)
     * locationClient.requestLocationUpdates(locationRequest, locationCallbackRef, null)
     */
    private class LocationCallbackReference(locationCallback: LocationCallback) : LocationCallback() {
        private val locationCallbackRef: WeakReference<LocationCallback> = WeakReference(locationCallback)

        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            locationCallbackRef.get()?.onLocationResult(locationResult)
        }
    }
}