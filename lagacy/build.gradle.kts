plugins {
    id("com.android.library")
}

android {
    namespace = "dev.nelson.mot.legacy"

    compileSdk = rootProject.extra["compileSDK"] as Int
    buildToolsVersion = rootProject.extra["buildToolsVersion"] as String

    defaultConfig {
        minSdk = rootProject.extra["minSDK"] as Int
        targetSdk = rootProject.extra["targetSDK"] as Int
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

repositories {
    google()
    mavenCentral()
    maven { url = uri("https://repo1.maven.org/maven2") }
    maven { url = uri("https://jitpack.io") }

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

    //android support
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("com.google.android.material:material:1.0.0-rc01")

    //test
    testImplementation("junit:junit:4.12")

    //other
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("android.arch.navigation:navigation-fragment:1.0.0-alpha07")
}
