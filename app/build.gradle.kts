import org.gradle.api.GradleException
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val appApplicationId: Provider<String> = providers.gradleProperty("app.applicationId")
val appVersionCode: Provider<Int> = providers.gradleProperty("app.versionCode")
    .map(String::toInt)
val appVersionName: Provider<String> = providers.gradleProperty("app.versionName")
val releaseStoreFile: Provider<String> = providers.gradleProperty("app.release.storeFile")
val releaseStorePassword: Provider<String> = providers.gradleProperty("app.release.storePassword")
val releaseKeyAlias: Provider<String> = providers.gradleProperty("app.release.keyAlias")
val releaseKeyPassword: Provider<String> = providers.gradleProperty("app.release.keyPassword")
val releaseStoreType: Provider<String> = providers.gradleProperty("app.release.storeType")

val releaseSigningProperties = mapOf(
    "app.release.storeFile" to releaseStoreFile,
    "app.release.storePassword" to releaseStorePassword,
    "app.release.keyAlias" to releaseKeyAlias,
    "app.release.keyPassword" to releaseKeyPassword
)
val hasCompleteReleaseSigning = releaseSigningProperties.values.all(Provider<String>::isPresent)
val requestedReleaseTask = gradle.startParameter.taskNames.any { taskName ->
    taskName.contains("release", ignoreCase = true)
}
if (requestedReleaseTask && !hasCompleteReleaseSigning) {
    val missingProperties = releaseSigningProperties
        .filterValues { !it.isPresent }
        .keys
        .sorted()

    throw GradleException(
        "Missing release signing properties: ${missingProperties.joinToString()}. " +
            "Provide them via gradle.properties or -P for release builds."
    )
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.futureworkshops.dotgolf.app"
    compileSdk = 36

    defaultConfig {
        applicationId = appApplicationId.get()
        minSdk = 30
        targetSdk = 36
        versionCode = appVersionCode.get()
        versionName = appVersionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("signing/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }

        if (hasCompleteReleaseSigning) {
            create("release") {
                storeFile = rootProject.file(releaseStoreFile.get())
                storePassword = releaseStorePassword.get()
                keyAlias = releaseKeyAlias.get()
                keyPassword = releaseKeyPassword.get()

                if (releaseStoreType.isPresent) {
                    storeType = releaseStoreType.get()
                }
            }
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }

        release {
            isMinifyEnabled = true
            if (hasCompleteReleaseSigning) {
                signingConfig = signingConfigs.getByName("release")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.hilt.android)
    implementation(libs.timber)
    implementation(project(":dotgolf"))
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
