package dev.nelson.mot.main.util

import androidx.compose.ui.graphics.Color

fun Color.toIntRepresentation(): Int {
    return android.graphics.Color.argb(
        (alpha * 255).toInt(),
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
}

fun List<Color>.toIntRepresentation(): List<Int> {
    return this.map { it.toIntRepresentation() }
}
