package dev.nelson.mot.main.domain.use_case.date_and_time

import dev.nelson.mot.main.domain.use_case.UseCaseSuspend
import kotlinx.datetime.Clock
import javax.inject.Inject

/**
 * return current time in milliseconds
 */
class GetCurrentTimeUseCase @Inject constructor() : UseCaseSuspend<Nothing?, Long> {
    // get oldest record in the db
    // get month(current, previous) time range (start, end)
    // get quarter(current, previous) time range (start, end)
    // get year(current, previous) time range (start, end)
    // get all time time range (start, end)

    override suspend fun execute(params: Nothing?): Long {
        return Clock.System.now().toEpochMilliseconds()
    }
}
