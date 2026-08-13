# Fix "Android BaseExtension not found" Error

The error `Android BaseExtension not found` is caused by an incompatibility between the legacy Hilt Gradle plugin (version 2.51.1) and the new Android Gradle Plugin (AGP) 9.0+. AGP 9.0 hides legacy DSL types like `BaseExtension` by default, which older plugins rely on.

## Proposed Changes

I will update Hilt to version `2.60.1`, which is compatible with AGP 9.x.

### Build Configuration

#### [MODIFY] [build.gradle.kts](file:///C:/Users/Admin/AndroidStudioProjects/iGigGuideSA/build.gradle.kts)
- Update Hilt plugin version from `2.51.1` to `2.60.1`.

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/Admin/AndroidStudioProjects/iGigGuideSA/app/build.gradle.kts)
- Update Hilt dependencies (`hilt-android` and `hilt-compiler`) from `2.51.1` to `2.60.1`.

### Project Properties (Optional/Fallback)

If updating Hilt is not desired or if other plugins also fail, we can opt out of the new AGP 9.0 DSL.

#### [MODIFY] [gradle.properties](file:///C:/Users/Admin/AndroidStudioProjects/iGigGuideSA/gradle.properties)
- Add `android.newDsl=false` to restore compatibility with legacy plugins.

## Verification Plan

### Automated Tests
- Trigger a Gradle Sync to verify that the `BaseExtension not found` error is resolved.
- Run `./gradlew assembleDebug` to ensure the project builds correctly.

### Manual Verification
- Check the "Build" output in Android Studio to confirm successful sync.
