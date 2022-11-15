package dev.nelson.mot.main.domain.use_case.date_and_time

import dev.nelson.mot.main.data.repository.CategoryRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

/**
 * return current time in milliseconds
 */
class GetCurrentTimeUseCase @Inject constructor(){
    // get oldest record in the db
    // get month(current, previous) time range (start, end)
    // get quarter(current, previous) time range (start, end)
    // get year(current, previous) time range (start, end)
    // get all time time range (start, end)

    suspend fun execute(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }
}