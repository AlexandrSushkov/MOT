package dev.nelson.mot.main.util

import android.content.Context

fun loadFileFromAssets(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use { it.readText() }
}