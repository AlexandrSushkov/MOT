package dev.nelson.mot.main.presentations.navigationcomponent

import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestUseCase @Inject constructor(private val testRepository: TestRepository) {

    fun test(): Observable<String> = testRepository.test()
}
