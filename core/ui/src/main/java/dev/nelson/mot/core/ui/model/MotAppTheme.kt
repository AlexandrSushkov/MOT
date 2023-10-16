package dev.nelson.mot.core.ui.model

sealed class MotAppTheme(val name: String) {
    data object System : MotAppTheme("System")
    data object Light : MotAppTheme("Light")
    data object Dark : MotAppTheme("Dark")

    companion object {
        val default
            get() = System

        fun fromString(name: String): MotAppTheme {
            return when (name) {
                Light.name -> Light
                Dark.name -> Dark
                else -> System
            }
        }

        fun getThemes(): List<MotAppTheme> {
            return listOf(System, Light, Dark)
        }
    }
}
