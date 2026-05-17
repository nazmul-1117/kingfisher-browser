plugins {
    id("com.android.application") version "8.5.2" apply false
    id("com.android.library") version "8.5.2" apply false

    // IMPORTANT: use ONE Kotlin version everywhere
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false

    // Compose plugin MUST match Kotlin version (NOT 1.9.24)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
}