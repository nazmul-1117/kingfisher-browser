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
        // Add this line so Gradle can find the GeckoView artifacts
        maven { url = uri("https://maven.mozilla.org/maven2/") }
    }
}

rootProject.name = "Kingfisher Browser"
include(":app")