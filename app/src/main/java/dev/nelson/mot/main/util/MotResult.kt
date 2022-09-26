package dev.nelson.mot.main.util

sealed class MotResult<out R> {
    object Loading : MotResult<Nothing>()
    data class Success<out T>(val data: T) : MotResult<T>()
    data class Error(val error: Exception) : MotResult<Nothing>()
}

val MotResult<*>.succeeded
    get() = this is MotResult.Success && data != null

fun <T> MotResult<T>.successOr(fallback: T): T {
    return (this as? MotResult.Success)?.data ?: fallback
}
