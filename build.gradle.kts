/**
 *  It defines dependencies that apply to all modules in your project.
 *  By default, the top-level build file uses the plugins block to define the Gradle
 *  dependencies that are common to all modules in the project.
 *  In addition, the top-level build file contains code to clean your build directory.
 */
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.android.library) apply false
}

val appId by extra("dev.nelson.mot")

// App
val versionName by extra("2.1.1") // X.Y.Z; X = Major, Y = minor, Z = Patch level
val versionCode by extra(16)

// SDK and tools
val compileSdk by extra(33)
val targetSdk by extra(33)
val minSdk by extra(33)
val buildToolsVersion by extra("30.0.3")

val javaVersion by extra(JavaVersion.VERSION_17)
val jvmTarget by extra("17")

// Dependencies
val testRunner by extra("dev.nelson.mot.main.MotTestRunner")

buildscript {
    dependencies {
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
        classpath(libs.google.services)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
