package co.lateralview.myapp.ui.util.date

import org.threeten.bp.Clock
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.Temporal

class DateUtils(private val clock: Clock = Clock.systemDefaultZone()) {

    fun getDateFromString(string: String): OffsetDateTime =
        OffsetDateTime.parse(string, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    fun getDateString(date: OffsetDateTime): String = date.format(DateTimeFormatter.ofPattern("EEEE d MMMM"))

    fun getTimeString(date: OffsetDateTime): String = date.format(DateTimeFormatter.ofPattern("HH:mm"))

    fun getTimeStringAMPM(date: OffsetDateTime): String = date.format(DateTimeFormatter.ofPattern("hh:mm a"))

    fun getMonthFromDate(date: LocalDate): String = date.format(DateTimeFormatter.ofPattern("MMMM"))

    fun getYearFromDate(date: LocalDate): String = date.format(DateTimeFormatter.ofPattern("YYYY"))

    fun getWeekDayFromDate(date: LocalDate): String = date.format(DateTimeFormatter.ofPattern("EEE"))

    fun getDayOfMonthFromDate(date: LocalDate): String = date.format(DateTimeFormatter.ofPattern("d"))

    fun getDaysBefore(date: OffsetDateTime, count: Int) = getDaysAfter(date.minusDays(count + 1L), count)

    fun getDaysAfter(date: OffsetDateTime, count: Int): List<OffsetDateTime> {
        val days = ArrayList<OffsetDateTime>()
        for (i in 1L..count) {
            days.add(date.plusDays(i))
        }
        return days
    }

    fun dateTimeDifference(date1: Temporal, date2: Temporal, unit: ChronoUnit): Long {
        return unit.between(date1, date2)
    }

    fun getCurrentDate(): OffsetDateTime = OffsetDateTime.now(clock)

    fun getCurrentDateTime(): LocalDateTime = LocalDateTime.now(clock)
}