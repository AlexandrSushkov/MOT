plugins {
    id("com.android.application")
    kotlin("android")
//    kotlin("parcelize")
    kotlin("kapt")
//    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
}
//apply(plugin: "com.android.application")
//apply(plugin: "kotlin-android")
//apply(plugin: "kotlin-parcelize")
//apply(plugin: "kotlin-kapt")
//apply(plugin: "dagger.hilt.android.plugin")
//apply(plugin: "androidx.navigation.safeargs")

android {
    namespace = "dev.nelson.mot.main"

    compileSdk = rootProject.extra["compileSDK"] as Int
    buildToolsVersion = rootProject.extra["buildToolsVersion"] as String

    defaultConfig {
        vectorDrawables {
            useSupportLibrary = true
        }
        applicationId = rootProject.extra["applicationId"] as String
        minSdk = rootProject.extra["minSDK"] as Int
        targetSdk = rootProject.extra["targetSDK"] as Int
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

    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }

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

    kapt {
        correctErrorTypes = true
    }

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
//    implementation project(":lagacy")
    implementation(project(":core"))
    implementation(project(":db"))
//    implementation project(":feature-payments")
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
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.test:runner:1.5.2")

    //material
    implementation("com.google.android.material:material:1.10.0-alpha01")
    implementation("androidx.core:core-splashscreen:1.0.0")

    //firebase
//    implementation platform ("com.google.firebase:firebase-bom:29.0.3")
//    implementation("com.google.firebase:firebase-analytics-ktx")

    //android support
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.appcompat:appcompat:1.7.0-alpha02")
    implementation("androidx.fragment:fragment-ktx:1.5.6")

    //navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.navigation:navigation-compose:2.5.3")

    //compose
    val compose_version = "1.4.1"
    implementation("androidx.compose.ui:ui:$compose_version")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:$compose_version")
    implementation("androidx.compose.runtime:runtime:$compose_version")
    implementation("androidx.compose.compiler:compiler:1.4.4")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:$compose_version")
    // Material Design
    implementation("androidx.compose.material:material:$compose_version")
    // Material design icons
    implementation("androidx.compose.material:material-icons-core:$compose_version")
    implementation("androidx.compose.material:material-icons-extended:$compose_version")
    // Integration with activities
    implementation("androidx.activity:activity-compose:1.7.0")
    // Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    // Integration with observables
    implementation("androidx.compose.runtime:runtime-livedata:$compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$compose_version")
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")

    //Lifecycle
    val lifecycle_version = "2.3.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // Preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //DI
    kapt("com.google.dagger:hilt-compiler:2.44")
    implementation("com.google.dagger:hilt-android:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.44")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.44")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    //network
//    implementation "com.google.code.gson:gson:2.9.0"
//    implementation "com.squareup.retrofit2:retrofit:2.9.0"
//    implementation "com.squareup.retrofit2:converter-gson:2.9.0"

    //unit test

//    testImplementation "org.junit.jupiter:junit-jupiter:5.7.1"
//    testCompileOnly "junit:junit:4.13.2"

    // android test
//    androidTestImplementation "org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version"
//    androidTestImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4"

//    kaptAndroidTest "junit:junit:4.13.2"
//    androidTestImplementation "androidx.test:core-ktx:1.4.0"

//    androidTestImplementation "androidx.test.ext:junit-ktx:1.1.3"
//    androidTestImplementation "androidx.test.ext:junit:1.1.3"
//    androidTestImplementation "androidx.test.espresso:espresso-core:3.4.0"
//    testImplementation "junit:junit:5.9.2"
//    testImplementation "org.junit.jupiter:junit-jupiter:5.9.2"

    //other
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
}
