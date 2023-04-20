plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "dev.nelson.mot.db"
    compileSdk = rootProject.extra["compileSDK"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSDK"] as Int
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    kapt {
        correctErrorTypes = true
    }
}

repositories {
    google()
    mavenCentral()
    maven { url = uri("https://repo1.maven.org/maven2") }
}

dependencies {
    implementation(project(":core"))

    val roomVersion = "2.5.1"
    kapt ("androidx.room:room-compiler:$roomVersion")
    api ("androidx.room:room-runtime:$roomVersion")
    api ("androidx.room:room-ktx:$roomVersion") //Kotlin Extensions and Coroutines support for Room
}
