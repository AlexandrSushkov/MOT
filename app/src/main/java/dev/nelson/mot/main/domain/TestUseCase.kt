package dev.nelson.mot.main.domain

import dev.nelson.mot.main.data.repository.TestRepository
import io.reactivex.Observable
import javax.inject.Inject

class TestUseCase @Inject constructor( private val repository: TestRepository) {

    fun getTest(): Observable<String> = repository.test()

}
