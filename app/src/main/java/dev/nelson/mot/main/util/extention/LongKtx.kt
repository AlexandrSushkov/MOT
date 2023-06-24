package dev.nelson.mot.main.util.extention

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val MONTH_YEAR_FORMAT = "MMMM yyyy"

fun Long.convertMillisecondsToDate(
    format: String = MONTH_YEAR_FORMAT
): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this

    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(calendar.time)
}
