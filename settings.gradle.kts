/**
 *  This settings file defines project-level repository settings and informs Gradle which modules it should
 *  include when building your app. Multi-module projects need to specify each module that should go into the final build.
 */
pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://repo1.maven.org/maven2") }
    }
}
rootProject.name = "MOT"

include(":app") //main app
include(":core") //core modules
//feature modules
include(":lagacy") //legacy module
include(":db")
