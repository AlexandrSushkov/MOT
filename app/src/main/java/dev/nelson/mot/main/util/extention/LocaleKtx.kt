package dev.nelson.mot.main.util.extention

import java.util.Locale

private const val EMOJI_UNICODE = 0x1F1A5

fun Locale.emojiFlag(): String = this.country
    .uppercase()
    .map { char -> Character.codePointAt("$char", 0) + EMOJI_UNICODE }
    .joinToString("") { String(Character.toChars(it)) }

fun Array<Locale>.filterDefaultCountries(): List<Locale> {
    return this.toList()
        .filter { it.country.isNotEmpty() }
        .filterNot { it.displayCountry.containsAny("Europe", "Lain America", "world", "Pseudo") }
        .sortedBy { it.displayCountry }
        .groupBy { it.displayCountry }
        .map { it.value.first() }
}

fun Locale.doesSearchMatch(search: String): Boolean {
    return this.displayCountry.contains(search, ignoreCase = true) ||
        this.country.contains(search, ignoreCase = true)
}
