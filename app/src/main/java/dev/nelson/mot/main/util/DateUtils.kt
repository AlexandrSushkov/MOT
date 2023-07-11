package dev.nelson.mot.main.util

import java.util.Calendar
import java.util.Date

object DateUtils {

    fun getCurrentDate(): Date = Calendar.getInstance().time

    fun createDateFromMills(dateInMills: Long): Date = Date(dateInMills)
}
