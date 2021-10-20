rootProject.name = "gli"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://raw.githubusercontent.com/kotlin-graphics/mary/master")
    }
}

gradle.rootProject {
    group = "kotlin.graphics"
    version = "0.8.3.0-16+25"
}