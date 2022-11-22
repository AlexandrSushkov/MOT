package dev.nelson.mot.main.domain.use_case.date_and_time

import javax.inject.Inject

/**
 * return 00:00 of the first day of the current month in milliseconds
 */
class GetStartOfCurrentMonthTimeUseCase @Inject constructor(
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
    private val getStartOfMonthTimeUseCase: GetStartOfMonthTimeUseCase
) {

    suspend fun execute(): Long {
        val currentTime = getCurrentTimeUseCase.execute()
        return getStartOfMonthTimeUseCase.execute(currentTime)
    }
}
