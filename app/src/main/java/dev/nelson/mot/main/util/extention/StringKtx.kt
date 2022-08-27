package dev.nelson.mot.main.util.extention


fun String.leaveOnlyDigits(): String {
    return Regex("[^0-9]").replace(this, "")
}
