package dev.nelson.mot.main.domain.usecase.date

import dev.nelson.mot.main.domain.usecase.base.UseCaseSuspend
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

/**
 * return 00:00 of the first day of the month that set by time, in milliseconds
 */
class GetStartOfMonthTimeUseCase @Inject constructor() : UseCaseSuspend<Long, Long> {

    override suspend fun execute(params: Long): Long {
        val systemTZ = TimeZone.currentSystemDefault()
        val date = Instant.fromEpochMilliseconds(params).toLocalDateTime(systemTZ).date
        val firstDayOfTheMonth = if (date.dayOfMonth > 1) {
            date.minus(date.dayOfMonth - 1, DateTimeUnit.DAY)
        } else {
            date
        }
        return firstDayOfTheMonth.atStartOfDayIn(systemTZ).toEpochMilliseconds()
    }
}
