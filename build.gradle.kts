/**
 *  It defines dependencies that apply to all modules in your project.
 *  By default, the top-level build file uses the plugins block to define the Gradle
 *  dependencies that are common to all modules in the project.
 *  In addition, the top-level build file contains code to clean your build directory.
 */
plugins {
    id("com.android.application") version "7.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
//    id ("com.google.gms:google-services") version "4.3.10" apply false
//    id ("com.neenbedankt.gradle.plugins:android-apt") version "1.8" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}

ext {
    extra["applicationId"] = "dev.nelson.mot"

    // App version
    extra["versionName"] = "1.0.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level
    extra["versionCode"] = 9

    // SDK and tools
    extra["compileSDK"] = 33
    extra["targetSDK"] = 33
    extra["minSDK"] = 32
    extra["buildToolsVersion"] = "30.0.3"

    // dependencies versions
    extra["kotlinVersion"] = "1.8.10"
    extra["testRunner"] = "dev.nelson.mot.main.MotTestRunner"

    // google
    extra["googleServices"] = "4.3.10"
}

buildscript {
    dependencies {
        val nav_version = "2.5.3"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
