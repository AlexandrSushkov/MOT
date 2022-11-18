package dev.nelson.mot.main.use_case.date_and_time

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.nelson.mot.main.domain.use_case.date_and_time.GetCurrentTimeUseCase
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class GetCurrentTimeUseCaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var useCase: GetCurrentTimeUseCase

    val currentTime = 1667230815345 // 31 Oct. 2022
    val startOfTheMonth = 1664571600000 // 1 Oct. 2022
    val startOfTheYear = 1640988000000 // 1 Jan. 2022

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun ex() {
        val result = useCase.execute()
        assertEquals(1, result)
    }

}
