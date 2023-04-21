plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    val appId: String by rootProject.extra
    namespace = "$appId.main"

    compileSdk = rootProject.extra["compileSdk"] as Int
    buildToolsVersion = rootProject.extra["buildToolsVersion"] as String

    defaultConfig {
        vectorDrawables {
            useSupportLibrary = true
        }

        applicationId = appId
        minSdk = rootProject.extra["minSdk"] as Int
        targetSdk = rootProject.extra["targetSdk"] as Int
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"] as String
        testInstrumentationRunner = rootProject.extra["testRunner"] as String
        compileSdkPreview = "UpsideDownCake"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    kotlinOptions { jvmTarget = "1.8" }

    composeOptions { kotlinCompilerExtensionVersion = "1.4.4" }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    kapt { correctErrorTypes = true }

    defaultConfig.multiDexEnabled = true
    android.buildFeatures.dataBinding = true
    android.buildFeatures.viewBinding = true
    android.buildFeatures.compose = true
}

repositories {
    google()
    mavenCentral()
    maven { url = uri("https://repo1.maven.org/maven2") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":db"))
    implementation(
        fileTree(
            mapOf(
                "dir" to "libs",
                "include" to listOf("*.jar")
            )
        )
    )

    implementation("com.google.android.gms:play-services-analytics-impl:18.0.2")
    implementation("androidx.compose.material3:material3:1.0.1")
    implementation("androidx.test:runner:1.5.2")
    implementation("com.google.android.material:material:1.10.0-alpha01")
    implementation("androidx.core:core-splashscreen:1.0.1")

    //android support
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.appcompat:appcompat:1.7.0-alpha02")
    implementation("androidx.fragment:fragment-ktx:1.5.7")

    //navigation
    val navVersion = "2.5.3"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.navigation:navigation-compose:$navVersion")

    //compose
    val composeVersion = "1.4.2"
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion") // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion") // Foundation (Border, Background, shapes, animations, etc.)
    implementation("androidx.compose.material:material:$composeVersion") // Material Design
    implementation("androidx.compose.material:material-icons-core:$composeVersion") // Material design icons
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.runtime:runtime:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion") // Integration with observables
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
    implementation("androidx.compose.compiler:compiler:1.4.6")
    implementation("androidx.activity:activity-compose:1.7.1") // Integration with activities
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1") // Integration with ViewModels
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")

    //Lifecycle
    val lifecycleVersion = "2.6.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // Preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //DI
    val hiltVersion = "2.44"
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    //other
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
}
