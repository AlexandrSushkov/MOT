package dev.nelson.mot.main.domain.use_case.date_and_time

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

/**
 * return 00:00 of the first day of the current month in milliseconds
 */
class GetStartOfCurrentMonthTimeUseCase @Inject constructor(private val getCurrentTimeUseCase: GetCurrentTimeUseCase) {

    suspend fun execute(): Long {
        val currentTime = getCurrentTimeUseCase.execute()
        val systemTZ = TimeZone.currentSystemDefault()

        val currentDate = Instant.fromEpochMilliseconds(currentTime).toLocalDateTime(systemTZ).date
        val currentDayOfMonth = currentDate.dayOfMonth
        return if (currentDayOfMonth > 1) {
            currentDate.minus(currentDayOfMonth - 1, DateTimeUnit.DAY)
        }else {
            currentDate
        }.atStartOfDayIn(systemTZ)
            .toEpochMilliseconds()
    }
}
