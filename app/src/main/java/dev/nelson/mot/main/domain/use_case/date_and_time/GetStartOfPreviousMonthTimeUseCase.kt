package dev.nelson.mot.main.domain.use_case.date_and_time

import javax.inject.Inject

/**
 * return 00:00 of the first day of the previous month in milliseconds
 */
class GetStartOfPreviousMonthTimeUseCase @Inject constructor() {

    suspend fun execute(): Long {
        // TODO: to be implemented soon
        return 1L
    }
}
