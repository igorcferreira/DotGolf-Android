import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt.android) apply false
}

subprojects {
    pluginManager.withPlugin("com.android.application") {
        extensions.configure<ApplicationExtension>("android") {
            lint {
                lintConfig = rootProject.file("lint.xml")
                abortOnError = true
                checkDependencies = true
                checkReleaseBuilds = true
                sarifReport = true
                xmlReport = true
            }
        }
    }

    pluginManager.withPlugin("com.android.library") {
        extensions.configure<LibraryExtension>("android") {
            lint {
                lintConfig = rootProject.file("lint.xml")
                abortOnError = true
                checkDependencies = true
                checkReleaseBuilds = true
                sarifReport = true
                xmlReport = true
            }
        }
    }
}
