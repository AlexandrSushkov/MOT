/**
 *  This settings file defines project-level repository settings and informs Gradle which modules it should
 *  include when building your app. Multi-module projects need to specify each module that should go into the final build.
 */
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.google.com") }
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://repo1.maven.org/maven2") }
    }
}

rootProject.name = "mot"

include(":app") //main app
include(":db")
include(":core:ui")
include(":core:test")
include(":core:common")
include(":feature:settings")
