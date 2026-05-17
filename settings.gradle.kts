// settings.gradle.kts - NO version catalogs
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
        maven("https://maven.mozilla.org/maven2/")
    }
    // ❌ Remove versionCatalogs block entirely
}

rootProject.name = "KingfisherBrowser"
include(":app")