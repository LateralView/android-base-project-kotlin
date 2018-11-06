package co.lateralview.myapp.infrastructure.manager.interfaces

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import io.reactivex.Completable

interface PermissionsManager {
    fun isPermissionGranted(grantResults: IntArray): Boolean
    fun hasLocationPermission(context: Context): Boolean
    fun checkLocationPermission(context: Context): Completable
    fun requestLocationPermission(fragment: Fragment, requestCode: Int)
    fun requestLocationPermission(activity: Activity, requestCode: Int)
}