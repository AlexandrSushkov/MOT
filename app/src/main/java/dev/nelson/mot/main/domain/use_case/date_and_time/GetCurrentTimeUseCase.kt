package dev.nelson.mot.main.domain.use_case.date_and_time

import kotlinx.datetime.Clock
import javax.inject.Inject

/**
 * return current time in milliseconds
 */
class GetCurrentTimeUseCase @Inject constructor() {
    // get oldest record in the db
    // get month(current, previous) time range (start, end)
    // get quarter(current, previous) time range (start, end)
    // get year(current, previous) time range (start, end)
    // get all time time range (start, end)

    suspend fun execute(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }
}