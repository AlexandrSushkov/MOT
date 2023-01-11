package dev.nelson.mot.main.domain.use_case.date_and_time

import dev.nelson.mot.main.domain.use_case.UseCaseSuspend
import dev.nelson.mot.main.domain.use_case.execute
import javax.inject.Inject

/**
 * return 00:00 of the first day of the current month in milliseconds
 */
class GetStartOfCurrentMonthTimeUseCase @Inject constructor(
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
    private val getStartOfMonthTimeUseCase: GetStartOfMonthTimeUseCase
) : UseCaseSuspend<Nothing?, Long> {

    override suspend fun execute(params: Nothing?): Long {
        val currentTime = getCurrentTimeUseCase.execute()
        return getStartOfMonthTimeUseCase.execute(currentTime)
    }
}
