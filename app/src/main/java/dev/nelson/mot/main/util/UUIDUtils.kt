package dev.nelson.mot.main.util

import java.util.UUID

object UUIDUtils {

    val randomKey: String
        get() = UUID.randomUUID().toString()
}
