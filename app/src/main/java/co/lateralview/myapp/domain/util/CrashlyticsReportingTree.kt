package co.lateralview.myapp.domain.util

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

class CrashlyticsReportingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // These logs will appear in Firebase Console
        if (priority == Log.ERROR && t != null) {
            Crashlytics.logException(t)
            return
        }

        Crashlytics.log("$tag: $message")
    }
}