package co.lateralview.myapp.ui.util

import co.lateralview.myapp.ui.util.date.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.threeten.bp.Clock
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.temporal.ChronoUnit

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class DateUtilsTest {

    private val dateUtils = DateUtils(Clock.fixed(Instant.parse("2018-08-25T08:05:00.000Z"),
        ZoneId.of("UTC")))

    @Test
    fun itShouldReturnDate() {
        val date = dateUtils.getDateFromString("2018-06-08T16:40:30-03:00")
        assertEquals(2018, date.year)
        assertEquals(6, date.month.value)
        assertEquals(8, date.dayOfMonth)
        assertEquals(16, date.hour)
        assertEquals(40, date.minute)
        assertEquals(30, date.second)
        assertEquals(ZoneOffset.ofHours(-3), date.offset)
    }

    @Test
    @Config(qualifiers = "en-rUS")
    fun itShouldReturnDateStringEN() {
        val date = OffsetDateTime.of(2018, 6, 11, 20, 10, 5, 0, ZoneOffset.ofHours(-3))
        assertEquals("Monday 11 June", dateUtils.getDateString(date))
    }

    @Test
    @Config(qualifiers = "es-rAR")
    fun itShouldReturnDateStringES() {
        val date = OffsetDateTime.of(2018, 6, 11, 20, 10, 5, 0, ZoneOffset.ofHours(-3))
        assertEquals("lunes 11 junio", dateUtils.getDateString(date))
    }

    @Test
    fun itShouldReturnTimeString() {
        val date = OffsetDateTime.of(2018, 6, 11, 20, 10, 5, 0, ZoneOffset.ofHours(-3))
        assertEquals("20:10", dateUtils.getTimeString(date))
    }

    @Test
    fun itShouldReturnTimeWithPMFormat() {
        val date = OffsetDateTime.of(2018, 6, 11, 20, 10, 5, 0, ZoneOffset.ofHours(-3))
        assertEquals("08:10 PM", dateUtils.getTimeStringAMPM(date))
    }

    @Test
    fun itShouldReturnTimeWithAMFormat() {
        val date = OffsetDateTime.of(2018, 6, 11, 8, 10, 5, 0, ZoneOffset.ofHours(-3))
        assertEquals("08:10 AM", dateUtils.getTimeStringAMPM(date))
    }

    @Test
    fun itShouldReturnCurrentDate() {
        val offsetDateTimeNowUTC = OffsetDateTime.of(2018, 8, 25, 8, 5, 0, 0, ZoneOffset.UTC)
        assertEquals(offsetDateTimeNowUTC, dateUtils.getCurrentDate())
    }

    @Test
    fun itShouldReturnCurrentDateTime() {
        val localDateTimeNow = LocalDateTime.of(2018, 8, 25, 8, 5)
        assertEquals(localDateTimeNow, dateUtils.getCurrentDateTime())
    }

    @Test
    @Config(qualifiers = "en-rUS")
    fun itShouldReturnMonthEN() {
        val date = LocalDate.of(2018, 6, 8)
        assertEquals("June", dateUtils.getMonthFromDate(date))
    }

    @Test
    @Config(qualifiers = "es-rAR")
    fun itShouldReturnMonthES() {
        val date = LocalDate.of(2018, 6, 8)
        assertEquals("junio", dateUtils.getMonthFromDate(date))
    }

    @Test
    fun itShouldReturnYear() {
        val date = LocalDate.of(2018, 6, 8)
        assertEquals("2018", dateUtils.getYearFromDate(date))
    }

    @Test
    @Config(qualifiers = "en-rUS")
    fun itShouldReturnWeekDayEN() {
        val date = LocalDate.of(2018, 6, 8)
        assertEquals("Fri", dateUtils.getWeekDayFromDate(date))
    }

    @Test
    @Config(qualifiers = "es-rAR")
    fun itShouldReturnWeekDayES() {
        val date = LocalDate.of(2018, 6, 8)
        assertEquals("vie", dateUtils.getWeekDayFromDate(date))
    }

    @Test
    fun itShouldReturnDayOfMonth() {
        val date = LocalDate.of(2018, 6, 8)
        assertEquals("8", dateUtils.getDayOfMonthFromDate(date))
    }

    @Test
    fun itShouldReturnDaysBefore() {
        val date = OffsetDateTime.of(2018, 6, 8, 16, 40, 0, 0, ZoneOffset.ofHours(-3))
        val daysBefore = dateUtils.getDaysBefore(date, 2)

        assertEquals(daysBefore.size, 2)
        assertEquals(daysBefore[0], OffsetDateTime.of(2018, 6, 6, 16, 40, 0, 0, ZoneOffset.ofHours(-3)))
        assertEquals(daysBefore[1], OffsetDateTime.of(2018, 6, 7, 16, 40, 0, 0, ZoneOffset.ofHours(-3)))
    }

    @Test
    fun itShouldReturnDaysAfter() {
        val date = OffsetDateTime.of(2018, 6, 8, 16, 40, 0, 0, ZoneOffset.ofHours(-3))
        val daysBefore = dateUtils.getDaysAfter(date, 2)

        assertEquals(daysBefore.size, 2)
        assertEquals(daysBefore[0], OffsetDateTime.of(2018, 6, 9, 16, 40, 0, 0, ZoneOffset.ofHours(-3)))
        assertEquals(daysBefore[1], OffsetDateTime.of(2018, 6, 10, 16, 40, 0, 0, ZoneOffset.ofHours(-3)))
    }

    @Test
    fun shouldReturn15Minutes() {
        val date1 = OffsetDateTime.of(2018, 6, 8, 16, 10, 0, 0, ZoneOffset.ofHours(-3))
        val date2 = OffsetDateTime.of(2018, 6, 8, 16, 25, 0, 0, ZoneOffset.ofHours(-3))
        assertEquals(15, dateUtils.dateTimeDifference(date1, date2, ChronoUnit.MINUTES))
    }
}
