package dev.nelson.mot.main.domain.use_case.base

interface UseCaseSuspend<P, T : Any> {

    suspend fun execute(params: P): T
}

suspend fun <T : Any> UseCaseSuspend<Nothing?, T>.execute() = execute(null)
