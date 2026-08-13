# Fix "Android BaseExtension not found" Error

The error `Android BaseExtension not found` occurs because the project is using **Android Gradle Plugin (AGP) 9.3.0**, which introduces a new DSL architecture that removes legacy internal classes like `BaseExtension`. The current version of Hilt (2.51.1) depends on these legacy classes and is incompatible with the new DSL by default.

## Proposed Changes

We will resolve this by either opting out of the new DSL temporarily or upgrading Hilt to a version that supports the new DSL. Upgrading Hilt is the recommended long-term solution.

### 1. Upgrade Hilt (Recommended)
We will update Hilt to version **2.60.1**, which includes compatibility fixes for AGP 9.x.

#### [MODIFY] [build.gradle.kts (root)](file:///C:/Users/Admin/AndroidStudioProjects/iGigGuideSA/build.gradle.kts)
- Update the Hilt plugin version from `2.51.1` to `2.60.1`.

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/Admin/AndroidStudioProjects/iGigGuideSA/app/build.gradle.kts)
- Update the `hilt-android` and `hilt-compiler` dependency versions to `2.60.1`.

### 2. Opt-out of New DSL (Quick Fix / Fallback)
If the Hilt upgrade is not desired or causes other issues, we can restore the legacy DSL behavior.

#### [MODIFY] [gradle.properties](file:///C:/Users/Admin/AndroidStudioProjects/iGigGuideSA/gradle.properties)
- Add `android.newDsl=false` to restore compatibility for plugins depending on `BaseExtension`.

## User Review Required

> [!IMPORTANT]
> AGP 9.3.0 is a very recent version. Upgrading Hilt to **2.60.1** is the best path forward to ensure compatibility with modern Android build tools.

> [!TIP]
> If you prefer to stay on Hilt 2.51.1 for now, we should only apply the `gradle.properties` change.

## Verification Plan

### Automated Tests
- Run Gradle Sync to verify the error is resolved.
- Run `gradle :app:assembleDebug` to ensure Hilt code generation still works.

### Manual Verification
- Verify that the IDE no longer shows sync errors in `app/build.gradle.kts`.
