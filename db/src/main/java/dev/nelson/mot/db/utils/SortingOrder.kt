package dev.nelson.mot.db.utils

sealed class SortingOrder {
    data object Ascending : SortingOrder()
    data object Descending : SortingOrder()
}
