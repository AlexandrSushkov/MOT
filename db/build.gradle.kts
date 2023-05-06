@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    val appId: String by rootProject.extra
    namespace = "$appId.db"
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig { minSdk = rootProject.extra["minSdk"] as Int }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
    maven { url = uri("https://repo1.maven.org/maven2") }
}

dependencies {
    implementation(project(":core:common"))

    ksp(libs.room.compiler)
    api(libs.room.runtime)
    api(libs.room.ktx) // Kotlin Extensions and Coroutines support for Room
}
