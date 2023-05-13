package dev.nelson.mot.main.util

/**
 * Used for presentation layer to pass data between view model and composable to show progress
 *
 * @param R
 * @constructor Create empty Mot result
 */
sealed class MotUiState<out R> {
    object Loading : MotUiState<Nothing>()
    data class Success<out T>(val data: T) : MotUiState<T>()
    data class Error(val error: Exception) : MotUiState<Nothing>()
}

val MotUiState<*>.succeeded
    get() = this is MotUiState.Success && data != null

fun <T> MotUiState<T>.successOr(fallback: T): T {
    return (this as? MotUiState.Success)?.data ?: fallback
}
