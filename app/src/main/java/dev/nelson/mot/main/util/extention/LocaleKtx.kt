package dev.nelson.mot.main.util.extention

import java.util.Locale

private const val EMOJI_UNICODE = 0x1F1A5

fun Locale.emojiFlag(): String = this.country
    .uppercase()
    .map { char -> Character.codePointAt("$char", 0) + EMOJI_UNICODE }
    .joinToString("") { String(Character.toChars(it)) }
