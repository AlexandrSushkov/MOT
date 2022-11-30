package dev.nelson.mot.main.domain.use_case.date_and_time

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import java.util.Calendar
import javax.inject.Inject

/**
 * return 00:00 of the first day of the previous month in milliseconds.
 */
class GetStartOfPreviousMonthTimeUseCase @Inject constructor(
    private val getStartOfMonthTimeUseCase: GetStartOfMonthTimeUseCase
) {

    fun execute(time: Long): Long {
        val systemTZ = TimeZone.currentSystemDefault()
        val date = Instant.fromEpochMilliseconds(time).toLocalDateTime(systemTZ).date
        return if (date.monthNumber == 1) {
            // month is Jan. Return 1 Dec of the previous year
            val previousYear = date.year - 1
            Calendar.getInstance()
                .apply { set(previousYear, 12, 1) }
                .time
                .time

        } else {
            // take the first day of the the previous month.
            val previousMonth = date.minus(1, DateTimeUnit.MONTH)
            val thisDayOneMonthBefore = previousMonth.atStartOfDayIn(systemTZ).toEpochMilliseconds()
            getStartOfMonthTimeUseCase.execute(thisDayOneMonthBefore)
        }
    }
}
