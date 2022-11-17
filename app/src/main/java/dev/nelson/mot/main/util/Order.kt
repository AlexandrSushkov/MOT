package dev.nelson.mot.main.util

sealed class Order {
    object Ascending : Order()
    object Descending : Order()
}
