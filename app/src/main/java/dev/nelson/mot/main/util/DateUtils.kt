package dev.nelson.mot.main.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    fun getCurrentDate(): Date = Calendar.getInstance().time

    fun createDateFromMills(dateInMills: Long): Date = Date(dateInMills)

}

fun Date.toFormattedDate(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}
