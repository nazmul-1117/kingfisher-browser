import java.io.ByteArrayOutputStream
import java.io.File

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.kingfisher.browser"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kingfisher.browser"
        minSdk = 26
        targetSdk = 35

        // Configuration-cache safe versioning
        versionCode = getGitCommitCount()
        versionName = getGitVersionName()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true // Required to enable Compose tooling/features
    }

    packaging {
        jniLibs {
            useLegacyPackaging = true // Kept for GeckoView compatibility
        }
    }
}

/* ========================================================
   GIT VERSIONING FUNCTIONS (Configuration Cache Compliant)
   ======================================================== */

fun getGitCommitCount(): Int {
    return providers.exec {
        commandLine("git", "rev-list", "--count", "HEAD")
    }.standardOutput.asText.map { it.trim().toIntOrNull() ?: 1 }.getOrElse(1)
}

fun getGitVersionName(): String {
    return try {

        val baseVersion = "3.0.0" // 👈 FORCE START VERSION

        val tagProcess = ProcessBuilder(
            "git",
            "describe",
            "--tags",
            "--abbrev=0"
        ).start()

        val tagOutput = tagProcess.inputStream.bufferedReader().readText().trim()
        tagProcess.waitFor()

        val latestTag = if (tagOutput.isNotEmpty()) tagOutput else baseVersion

        val baseParts = latestTag.split(".").map { it.toIntOrNull() ?: 0 }

        val major = baseParts.getOrElse(0) { 3 }
        val minor = baseParts.getOrElse(1) { 0 }
        val patch = baseParts.getOrElse(2) { 0 }

        val countProcess = ProcessBuilder(
            "git",
            "rev-list",
            "$latestTag..HEAD",
            "--count"
        ).start()

        val commitsAfterTag = countProcess.inputStream.bufferedReader()
            .readText()
            .trim()
            .toIntOrNull() ?: 0

        countProcess.waitFor()

        val finalPatch = patch + commitsAfterTag

        "$major.$minor.$finalPatch"

    } catch (e: Exception) {
        "3.0.0" // 👈 IMPORTANT FALLBACK
    }
}


dependencies {
    implementation(platform(libs.compose.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.icons)

    implementation(libs.lifecycle.viewmodel.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.coil.compose)
    implementation(libs.kotlinx.coroutines.android)

    // GeckoView Engine
    implementation(libs.geckoview)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}