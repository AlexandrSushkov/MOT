package dev.nelson.mot.main.util

sealed class SortingOrder {
    object Ascending : SortingOrder()
    object Descending : SortingOrder()
}
