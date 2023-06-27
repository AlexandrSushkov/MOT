@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    val appId: String by rootProject.extra
    namespace = "$appId.core.test"
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdk"] as Int
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    kotlinOptions { jvmTarget = rootProject.extra["jvmTarget"] as String }

    compileOptions {
        val javaVersion: JavaVersion by rootProject.extra
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}

dependencies {
    testApi(libs.junit)
    testApi(libs.coroutines.test)
    androidTestApi(libs.androidx.test.ext.junit)
    androidTestApi(libs.espresso.core)
}
