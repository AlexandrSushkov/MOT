package dev.nelson.mot.main.util.extention

import dev.nelson.mot.main.util.constant.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Convert epoch time in milliseconds to date string
 * @param format date format. If null, [Constants.MONTH_YEAR_DATE_PATTERN] is used
 * @return date string
 */
fun Long.convertMillisecondsToDate(
    format: String = Constants.MONTH_YEAR_DATE_PATTERN
): String {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@convertMillisecondsToDate
    }

    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(calendar.time)
}
