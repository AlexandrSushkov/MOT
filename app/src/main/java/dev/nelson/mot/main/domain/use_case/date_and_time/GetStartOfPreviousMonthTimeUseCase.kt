package dev.nelson.mot.main.domain.use_case.date_and_time

import dev.nelson.mot.main.domain.use_case.base.UseCaseSuspend
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*
import javax.inject.Inject

/**
 * return 00:00 of the first day of the previous month in milliseconds.
 */
class GetStartOfPreviousMonthTimeUseCase @Inject constructor(
    private val getStartOfMonthTimeUseCase: GetStartOfMonthTimeUseCase
) : UseCaseSuspend<Long, Long> {

    override suspend fun execute(params: Long): Long {
        val systemTZ = TimeZone.currentSystemDefault()
        val date = Instant.fromEpochMilliseconds(params).toLocalDateTime(systemTZ).date
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
