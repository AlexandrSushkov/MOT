package dev.nelson.mot.db.utils

sealed class SortingOrder {
    object Ascending : SortingOrder()
    object Descending : SortingOrder()
}
