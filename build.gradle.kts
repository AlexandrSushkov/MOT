/**
 *  It defines dependencies that apply to all modules in your project.
 *  By default, the top-level build file uses the plugins block to define the Gradle
 *  dependencies that are common to all modules in the project.
 *  In addition, the top-level build file contains code to clean your build directory.
 */
plugins {
    id("com.android.application") version "7.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}

val appId by extra("dev.nelson.mot")

// App
val versionName by extra("1.0.0") // X.Y.Z; X = Major, Y = minor, Z = Patch level
val versionCode by extra(9)

// SDK and tools
val compileSdk by extra(33)
val targetSdk by extra(33)
val minSdk by extra(33)
val buildToolsVersion by extra("30.0.3")

// Dependencies
val kotlinVersion by extra("1.8.10")
val testRunner by extra("dev.nelson.mot.main.MotTestRunner")

buildscript {
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
