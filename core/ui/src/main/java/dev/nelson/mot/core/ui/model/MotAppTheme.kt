package dev.nelson.mot.core.ui.model

sealed class MotAppTheme(val name: String) {
    object System : MotAppTheme("System")
    object Light : MotAppTheme("Light")
    object Dark : MotAppTheme("Dark")

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
