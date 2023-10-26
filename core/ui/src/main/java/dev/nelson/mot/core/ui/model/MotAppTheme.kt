package dev.nelson.mot.core.ui.model

sealed class MotAppTheme {
    data object System : MotAppTheme()
    data object Light : MotAppTheme()
    data object Dark : MotAppTheme()

    companion object {
        val default
            get() = System

        fun fromString(name: String): MotAppTheme {
            return when (name) {
                Light.javaClass.simpleName -> Light
                Dark.javaClass.simpleName -> Dark
                else -> System
            }
        }

        fun getThemes(): List<MotAppTheme> {
            return listOf(System, Light, Dark)
        }
    }
}
