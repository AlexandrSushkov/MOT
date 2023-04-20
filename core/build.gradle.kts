plugins {
    id ("com.android.library")
    id ("org.jetbrains.kotlin.android")
}

android {
    namespace = "dev.nelson.mot.core"
    compileSdk = rootProject.extra["compileSDK"] as Int

    defaultConfig{
        minSdk = rootProject.extra["minSDK"] as Int
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(
        fileTree(
            mapOf(
                "dir" to "libs",
                "include" to listOf("*.jar")
            )
        )
    )
    
    api ("com.google.code.gson:gson:2.9.0")
    api ("androidx.core:core-ktx:1.10.0")

    // Kotlin Coroutines
    api ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testApi ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    api ("com.jakewharton.timber:timber:4.7.1")

//    testApi ("junit:junit:5.9.2")
//    testApi ("org.junit.jupiter:junit-jupiter:5.9.2")
}
