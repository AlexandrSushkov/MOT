package dev.nelson.mot.main.domain

import dev.nelson.mot.main.domain.use_case.date_and_time.GetCurrentTimeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class GetCurrentTimeUseCaseTest {

    private val useCase: GetCurrentTimeUseCase = GetCurrentTimeUseCase()

    @Test
    fun `GetCurrentTimeUseCaseTest return current date and time`() = runTest {
        val today = Calendar.getInstance()
        val resultTime = useCase.execute()
        val resultDay = Calendar.getInstance().apply {
            timeInMillis = resultTime
        }
        assertEquals(today.get(Calendar.YEAR), resultDay.get(Calendar.YEAR))
        assertEquals(today.get(Calendar.MONTH), resultDay.get(Calendar.MONTH))
        assertEquals(today.get(Calendar.DAY_OF_MONTH), resultDay.get(Calendar.DAY_OF_MONTH))
        assertEquals(today.get(Calendar.HOUR_OF_DAY), resultDay.get(Calendar.HOUR_OF_DAY))
        assertEquals(today.get(Calendar.MINUTE), resultDay.get(Calendar.MINUTE))
        assertEquals(today.get(Calendar.SECOND), resultDay.get(Calendar.SECOND))
    }
}
