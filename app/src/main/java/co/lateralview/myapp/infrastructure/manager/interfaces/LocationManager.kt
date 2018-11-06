package co.lateralview.myapp.infrastructure.manager.interfaces

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.Single

interface LocationManager {
    fun startLocationUpdates(): Observable<LatLng>
    fun getLastLocation(): Single<LatLng>
    fun getLastLocationOrEmpty(): Single<LatLng>

    fun distanceBetween(startLatLng: LatLng, endLatLng: LatLng, unit: DistanceUnit): Double
    fun getLocationFromName(locationName: String): Single<LatLng>

    enum class DistanceUnit(val multiplier: Double) {
        METERS(1.0),
        MILES(0.000621371),
        FEET(3.28084)
    }
}