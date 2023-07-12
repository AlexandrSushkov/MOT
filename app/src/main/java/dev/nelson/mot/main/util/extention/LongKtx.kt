package dev.nelson.mot.main.util.extention

import dev.nelson.mot.main.util.constant.Constants
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Convert epoch time in milliseconds to date string
 * @param format date format. If null, [Constants.MONTH_YEAR_DATE_PATTERN] is used
 * @param locale locale. If null, [Locale.getDefault] is used
 * @return date string
 */
fun Long.formatMillsToDateText(
    format: String = Constants.MONTH_YEAR_DATE_PATTERN,
    locale: Locale = Locale.getDefault()
): String {
    val instant = Instant.ofEpochMilli(this)
    val zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
    return DateTimeFormatter.ofPattern(format, locale)
        .format(zonedDateTime)
}
