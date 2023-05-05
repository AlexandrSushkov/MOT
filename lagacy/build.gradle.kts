plugins {
    alias(libs.plugins.androidLibrary)
}

android {
    val appId: String by rootProject.extra
    namespace = "$appId.legacy"

    compileSdk = rootProject.extra["compileSdk"] as Int
    buildToolsVersion = rootProject.extra["buildToolsVersion"] as String

    defaultConfig {
        minSdk = rootProject.extra["minSdk"] as Int
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
    implementation("com.google.android.material:material:1.0.0")

    //test
    testImplementation("junit:junit:4.12")

    //other
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("android.arch.navigation:navigation-fragment:1.0.0")
}
