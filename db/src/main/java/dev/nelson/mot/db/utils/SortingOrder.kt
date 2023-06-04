package dev.nelson.mot.db.utils

sealed class SortingOrder(value: String) {
    object Ascending : SortingOrder("AES")
    object Descending : SortingOrder("DESC")
}
