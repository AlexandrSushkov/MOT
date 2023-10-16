package dev.nelson.mot.main.domain.usecase.date

import javax.inject.Inject

/**
 * return 00:00 of the first day of the current year in milliseconds
 */
class GetStartOfCurrentYearTimeUseCase @Inject constructor() {

    suspend fun execute(): Long {
        // TODO: to be implemented soon
        return 1L
    }
}
