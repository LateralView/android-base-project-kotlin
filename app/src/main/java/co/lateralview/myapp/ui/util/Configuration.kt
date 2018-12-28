package co.lateralview.myapp.ui.util

import co.lateralview.myapp.BuildConfig
import org.threeten.bp.Duration

object Configuration {
    /**
     * Disable if you don't want to allow the user to mock its location with apps like FakeGps
     */
    const val IS_MOCK_LOCATION_ALLOWED = BuildConfig.MOCK_LOCATION_ALLOWED

    val LOCATION_UPDATES_INTERVAL = Duration.ofSeconds(10).toMillis()
    val LOCATION_UPDATES_FASTEST_INTERVAL = Duration.ofSeconds(5).toMillis()
}