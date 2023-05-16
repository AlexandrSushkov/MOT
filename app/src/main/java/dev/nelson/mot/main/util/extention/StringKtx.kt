package dev.nelson.mot.main.util.extention


fun String.leaveOnlyDigits(): String {
    return Regex("[^0-9]").replace(this, "")
}

fun String?.containsAny(vararg values: String, ignoreCase: Boolean = false): Boolean {
    return this?.let { values.any { this.contains(it, ignoreCase = ignoreCase) } } ?: false
}
