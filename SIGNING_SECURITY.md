# Signing Configuration Security

## Overview
This project uses `local.properties` to store sensitive signing configuration credentials. This file is automatically excluded from version control via `.gitignore`, ensuring your passwords and keystore information are never committed to GitHub.

## Setup Instructions

### For New Developers

1. Copy the `local.properties.template` file to `local.properties`:
   ```bash
   cp local.properties.template local.properties
   ```

2. Edit `local.properties` and fill in your actual values:
   ```properties
   sdk.dir=/path/to/your/android/sdk
   
   # Signing configuration
   KEYSTORE_FILE=answerSheetKey.jks
   KEY_ALIAS=your_key_alias
   KEY_PASSWORD=your_key_password
   KEYSTORE_PASSWORD=your_keystore_password
   ```

3. Ensure the keystore file (`answerSheetKey.jks`) is in the `app/` directory.

### What's Protected

The following sensitive information is stored in `local.properties`:
- `KEY_ALIAS`: The alias of your signing key
- `KEY_PASSWORD`: The password for your signing key
- `KEYSTORE_FILE`: The path to your keystore file
- `KEYSTORE_PASSWORD`: The password for your keystore

### Files That Should NOT Be Committed

- `local.properties` (contains sensitive credentials)
- `answerSheetKey.jks` (your keystore file)
- Any `.jks`, `.keystore` files

### Files That SHOULD Be Committed

- `local.properties.template` (template without actual credentials)
- `build.gradle.kts` (reads from local.properties)
- `.gitignore` (ensures local.properties is excluded)

## How It Works

The `build.gradle.kts` file reads credentials from `local.properties`:

```kotlin
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

signingConfigs {
    register("sit") {
        keyAlias = localProperties.getProperty("KEY_ALIAS")
        keyPassword = localProperties.getProperty("KEY_PASSWORD")
        storeFile = file(localProperties.getProperty("KEYSTORE_FILE"))
        storePassword = localProperties.getProperty("KEYSTORE_PASSWORD")
    }
}
```

## Running Release Builds

To build a release version of the app:

```bash
./gradlew assembleRelease
```

The APK will be generated at: `app/build/outputs/apk/release/app-release.apk`

## CI/CD Setup

For continuous integration environments (GitHub Actions, Jenkins, etc.), you can inject these values as environment variables or secrets and create the `local.properties` file dynamically during the build process.

Example for GitHub Actions:
```yaml
- name: Create local.properties
  run: |
    echo "KEY_ALIAS=${{ secrets.KEY_ALIAS }}" >> local.properties
    echo "KEY_PASSWORD=${{ secrets.KEY_PASSWORD }}" >> local.properties
    echo "KEYSTORE_FILE=answerSheetKey.jks" >> local.properties
    echo "KEYSTORE_PASSWORD=${{ secrets.KEYSTORE_PASSWORD }}" >> local.properties
```

