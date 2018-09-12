package co.lateralview.myapp.infrastructure.manager

import android.content.Context
import android.net.ConnectivityManager

class InternetManager(context: Context) {
    private val mConnectivityManager: ConnectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isOnline: Boolean?
        get() {
            val netInfo = mConnectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }

    fun onWifi(): Boolean {
        return !mConnectivityManager.isActiveNetworkMetered
    }
}
