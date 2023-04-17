package dev.nelson.mot.main.domain.use_case.base

import kotlinx.coroutines.flow.Flow

interface UseCaseFlow<P, T : Any> {

    fun execute(params: P): Flow<T>
}