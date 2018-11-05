package co.lateralview.myapp.infrastructure.manager.implementation

import android.net.ConnectivityManager

class InternetManager(private val connectivityManager: ConnectivityManager) {

    val isOnline: Boolean
        get() {
            val netInfo = connectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }

    fun onWifi(): Boolean {
        return !connectivityManager.isActiveNetworkMetered
    }
}
