rootProject.name = "preloader"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("plugin")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

include("sample")