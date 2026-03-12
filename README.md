# DotGolf Android

DotGolf Android is structured as a white-label Android platform with a thin app wrapper, a reusable product library, and a separate theming/resource module.

## Responsibilities

The project is split into three Gradle modules:

1. `app`
Thin Android application wrapper.
Owns the launcher application, app-level signing, release packaging, and the top-level navigation host.

2. `dotgolf`
Reusable Android library module containing product UI, ViewModels, dependency injection, networking, and domain/data wiring.
This module uses Hilt, Retrofit, Kotlin Serialization, Navigation3, and Compose.

3. `theme`
Android resources/theming module consumed by `dotgolf`.
Holds app resources such as colors, themes, strings, and launcher assets to keep white-label customization isolated.

## Project Structure

```text
DotGolf-Android/
├─ app/                         # Android app wrapper module
├─ dotgolf/                     # Core product Android library
├─ theme/                       # Theme/resources Android library
├─ docs/
│  └─ CI.md                     # Bitrise CI/CD documentation
├─ bitrise.yml                  # Local Bitrise workflow definition
├─ gradle/
│  └─ libs.versions.toml        # Shared dependency/version catalog
├─ build.gradle.kts             # Root shared build configuration
├─ settings.gradle.kts          # Module inclusion
└─ gradle.properties            # Shared Gradle config and app metadata
```

## Build And Test

Common local commands:

- `./gradlew :app:assembleDebug`
- `./gradlew :app:assembleRelease`
- `./gradlew :app:lintDebug`
- `./gradlew :dotgolf:lintDebug`
- `./gradlew :theme:lintDebug`
- `./gradlew :dotgolf:assembleDebugAndroidTest`

## App Metadata And Signing

The app module reads these values from `gradle.properties` or `-P...` overrides:

- `app.applicationId`
- `app.versionCode`
- `app.versionName`

Release signing is also property-driven so CI/CD can inject credentials:

- `app.release.storeFile`
- `app.release.storePassword`
- `app.release.keyAlias`
- `app.release.keyPassword`
- `app.release.storeType` optional

Debug builds use a local copy of the default Android debug keystore at:

- `app/signing/debug.keystore`

## Linting

Android Lint is enabled for all Android modules and uses a shared root `lint.xml`.

The current rules include:

- `LogNotTimber` as an error
- typography quote/fraction checks disabled

## White-Label CI/CD

Bitrise pipelines orchestrate build, lint, tests, theming, signing, and release packaging.

Detailed CI/CD documentation lives in [docs/CI.md](docs/CI.md), including:

- [Pipelines overview](docs/CI.md#pipelines)
- [Workflows](docs/CI.md#workflows)
- [Adding a new app to the deploy pipeline](docs/CI.md#add-a-new-app-to-the-deploy-pipeline)
- [Step bundles](docs/CI.md#step-bundles)
- [Required environment variables and secrets](docs/CI.md#required-environment-variables-and-secrets)
- [Notes](docs/CI.md#notes)
