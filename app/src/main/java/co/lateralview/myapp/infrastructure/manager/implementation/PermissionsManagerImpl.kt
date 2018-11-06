package co.lateralview.myapp.infrastructure.manager.implementation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import co.lateralview.myapp.infrastructure.manager.interfaces.PermissionsManager
import io.reactivex.Completable

class PermissionsManagerImpl : PermissionsManager {

    override fun isPermissionGranted(grantResults: IntArray): Boolean {
        return grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED
    }

    override fun hasLocationPermission(context: Context): Boolean {
        return hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun checkLocationPermission(context: Context): Completable {
        return Completable.create { emitter ->
            if (hasLocationPermission(context)) {
                emitter.onComplete()
            } else {
                emitter.onError(SecurityException())
            }
        }
    }

    override fun requestLocationPermission(fragment: Fragment, requestCode: Int) {
        fragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), requestCode)
    }

    override fun requestLocationPermission(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), requestCode)
    }

    private fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}