package co.lateralview.myapp.infrastructure.manager.interfaces

import android.content.Context
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime

interface CalendarManager {
    fun addToCalendar(
        context: Context,
        eventName: String,
        startDate: OffsetDateTime,
        duration: Duration,
        address: String,
        rule: String? = null
    )
}