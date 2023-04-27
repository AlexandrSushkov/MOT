plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    val appId: String by rootProject.extra
    namespace = "$appId.db"
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig { minSdk = rootProject.extra["minSdk"] as Int }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = "17" }
}

repositories {
    google()
    mavenCentral()
    maven { url = uri("https://repo1.maven.org/maven2") }
}

dependencies {
    implementation(project(":core"))

    val roomVersion = "2.5.1"
    ksp("androidx.room:room-compiler:$roomVersion")
    api("androidx.room:room-runtime:$roomVersion")
    api("androidx.room:room-ktx:$roomVersion") //Kotlin Extensions and Coroutines support for Room
}
