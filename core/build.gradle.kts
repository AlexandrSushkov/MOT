plugins {
    id ("com.android.library")
    id ("org.jetbrains.kotlin.android")
}

android {
    val appId: String by rootProject.extra
    namespace = "$appId.core"
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig{
        minSdk = rootProject.extra["minSdk"] as Int
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
    api ("com.jakewharton.timber:timber:4.7.1")

    // Kotlin Coroutines
    val coroutinesVersion = "1.6.4"
    api ("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    testApi ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

    val junitVersion= "5.9.2"
    testApi ("junit:junit:$junitVersion")
    testApi ("org.junit.jupiter:junit-jupiter:$junitVersion")
}
