@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
}

android {
    val appId: String by rootProject.extra
    namespace = "$appId.core"
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdk"] as Int
    }

    compileOptions {
        val javaVersion: JavaVersion by rootProject.extra
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    kotlinOptions { jvmTarget = rootProject.extra["jvmTarget"] as String }
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

    api(libs.androidx.ktx)
    api(libs.gson)
    api(libs.timber)
    api(libs.coroutines.core)
    testApi(libs.coroutines.test)
    testApi(libs.junit)
}
