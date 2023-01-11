package dev.nelson.mot.main.domain.use_case

import kotlinx.coroutines.flow.Flow

interface UseCaseSuspend<P, T : Any> {

    suspend fun execute(params: P): T
}

interface UseCaseFlow<P, T : Any> {

    fun execute(params: P): Flow<T>
}

suspend fun <T : Any> UseCaseSuspend<Nothing?, T>.execute() = execute(null)
