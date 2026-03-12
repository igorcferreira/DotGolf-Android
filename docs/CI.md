# Android CI/CD

This repository uses Bitrise for CI/CD. The checked-in CI definition is [bitrise.yml](../bitrise.yml).

## Pipelines

The local Bitrise config defines three pipelines:

- `test`
  - Trigger: pull requests targeting any branch.
  - Runs `setup`, then sharded `run_tests`, then sharded `run_instrumented_tests`, then `tear_down`.

- `preview`
  - Trigger: pushes to `dev`.
  - Runs `setup`, then `build_dot_golf`, then `tear_down`.

- `deploy`
  - Trigger: pushes to `main`.
  - Runs `setup`, then sharded `run_tests`, then `build_dot_golf`, then `tear_down`.

## Workflows

### `setup`

Prepares the CI environment:

- clone repository
- select Java 21
- restore Gradle cache
- install missing Android SDK tools

### `run_tests`

Runs:

- lint via `Run Linter`
- local JVM/unit tests via `Run Unit Tests`

This workflow is sharded using `TEST_SHARD_COUNT`.

### `run_instrumented_tests`

Runs:

- lint via `Run Linter`
- connected Android tests via `Run Connected Tests`

This workflow is also sharded using `TEST_SHARD_COUNT`.

### `build_dot_golf`

Builds the release app artifacts:

1. apply theme resources
2. download the release keystore
3. build the app with Gradle

The build step runs:

```bash
./gradlew :app:assembleRelease :app:bundleRelease \
  -Papp.applicationId=... \
  -Papp.versionCode=... \
  -Papp.versionName=... \
  -Papp.release.storeFile=... \
  -Papp.release.storePassword=... \
  -Papp.release.keyAlias=... \
  -Papp.release.keyPassword=...
```

That matches the property-driven release signing configuration in the Android app module.

### `tear_down`

Final CI cleanup:

- save Gradle cache
- deploy artifacts to Bitrise

## Add A New App To The Deploy Pipeline

To add a new app variant to the CD `deploy` pipeline, duplicate the existing `build_dot_golf` workflow and then customize only the app-specific inputs.

Example:

1. Duplicate `build_dot_golf` to a new workflow, for example `build_spain`.
2. In `build_spain`, keep the same workflow structure:
   - `Apply Theme`
   - `Download the Keystore`
   - `Build the release application`
3. Update the `Apply Theme` step bundle reference inputs:
   - `RESOURCE_URL`
4. Update the `Build the release application` step bundle reference inputs:
   - `APPLICATION_ID`
   - `APP_VERSION_CODE` if needed
   - `APP_VERSION_NAME` if needed
   - `APP_KEYSTORE_PATH`
   - `KEYSTORE_PASSWORD`
   - `KEYSTORE_ALIAS`
   - `KEYSTORE_ALIAS_PASSWORD`
5. If the new app uses a different keystore artifact, update the `Download the Keystore` step bundle reference inputs:
   - `KEYSTORE_NAME`
   - and any app-specific access/bucket values if they differ
6. Add the duplicated workflow to the `deploy` pipeline with `depends_on: [run_tests]`.
7. Update `tear_down.depends_on` so it waits for all app build workflows.
8. Prefer doing this through the Bitrise workflow UI:
   - duplicate the workflow
   - edit the step bundle inputs in the duplicated workflow
   - add the workflow to the `deploy` pipeline
   - update the pipeline dependencies visually

The important rule is to duplicate `build_dot_golf`, keep the same sequence, and update only the app-specific inputs passed into `Apply Theme` and `Build the release application` for the new app variant.

## Step Bundles

The Bitrise config uses shared step bundles. In the Bitrise workflow editor, prefer the visible step bundle titles below when navigating and configuring workflows.

### Prepare Local files

- `git-clone`
- `set-java-version` to Java 21
- `restore-gradle-cache`
- `install-missing-android-tools`

Use this in workflows that should execute Android Lint before building or testing.

### Run Linter

Runs Android Lint through the Bitrise `android-lint` step against the `Debug` variant.

### Run Unit Tests

Runs local JVM tests with the Bitrise `android-unit-test` step against the `Debug` variant.

### Run Connected Tests

- create/start emulator
- wait for emulator
- run `:app:connectedAndroidTest` with sharding arguments

Use this in workflows that need instrumented test execution on an emulator.

### Download the Keystore

Downloads the release keystore from S3 into the Bitrise workspace.

Use this in release workflows that need signing material before the build step.

### Apply Theme

Downloads a zipped resource bundle and unpacks it into:

- `theme/src/main/res`

This is the white-label theming injection point for Android resources.

Use this in white-label release workflows to swap app-specific resources through the Bitrise UI by changing the `RESOURCE_URL` input.

### Build the release application

Invokes the release Gradle build and passes app/signing/version properties into the app module.

Use this in release workflows to configure application ID, versioning, keystore path, and signing passwords through the Bitrise UI.

### Clean the environment

- save Gradle cache
- upload build artifacts to Bitrise

## Required Environment Variables And Secrets

The current Bitrise file expects these inputs or environment variables at runtime.

### General CI

- `TEST_SHARD_COUNT`
- `MODULE`
- `VARIANT`
- `APP_VERSION_NAME`

### Theme Application

- `BITRISEIO_RES_ZIP_URL`

### Release Signing

- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `INTERNAL_KEYSTORE_PASSWORD`
- `INTERNAL_KEYALIAS_PASSWORD`

### Build Inputs

- `BITRISE_BUILD_NUMBER`
- `APP_VERSION_NAME`
- release application ID
- keystore alias
- keystore path produced by the download step

## Notes

- The app module is a thin launcher/package wrapper; most product logic lives in `:dotgolf`.
- The `:theme` module is the white-label customization boundary for Android resources.
- Shared Android Lint configuration is applied across modules from the root Gradle build.
- Release builds are minified and signed through Gradle properties so Bitrise can inject signing at build time.
- This document reflects the checked-in `bitrise.yml`; if the Bitrise UI diverges from the file, update the repo copy to keep local documentation accurate.
