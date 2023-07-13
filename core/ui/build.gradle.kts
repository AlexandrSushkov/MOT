/**
 * General ui elements for the project
 */
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    val appId: String by rootProject.extra
    namespace = "$appId.core.ui"
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdk"] as Int
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    kotlinOptions { jvmTarget = rootProject.extra["jvmTarget"] as String }

    compileOptions {
        val javaVersion: JavaVersion by rootProject.extra
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    composeOptions { kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get() }

    android.buildFeatures.compose = true
}

dependencies {
    implementation(project(":core:test"))

    api(libs.androidx.core.ktx)

    //compose
    api(libs.compose.ui)
    api(libs.compose.ui.tooling) // Tooling support (Previews, etc.)
    api(libs.compose.ui.tooling.preview)
    api(libs.compose.foundation) // Foundation (Border, Background, shapes, animations, etc.)
    api(libs.compose.material3) // Material Design components
    api(libs.compose.material.icons.core) // Material design icons
    api(libs.compose.material.icons.extended)
    api(libs.compose.runtime)
    api(libs.compose.runtime.livedata) // Integration with observables
    api(libs.compose.compiler)

    //accompanist
    api(libs.accompanist.systemuicontroller)
}
