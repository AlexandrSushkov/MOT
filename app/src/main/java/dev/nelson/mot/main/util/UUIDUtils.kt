package dev.nelson.mot.main.util

import java.util.UUID

object UUIDUtils {

    val getRandomKey: String
        get() = UUID.randomUUID().toString()
}
