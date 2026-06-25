# CI/CD

Two GitHub Actions workflows live in `.github/workflows/`.

## `ci.yml` — runs on every pull request to `main` (and pushes to non-`main` branches)

1. Sets up JDK 17 + Gradle (with caching)
2. Writes `local.properties` from the `TMDB_API_KEY` secret
3. Runs Detekt, unit tests (`testDebugUnitTest`), and assembles the debug APK
4. Uploads test/Detekt reports as a build artifact

## `deploy.yml` — runs on push to `main` (i.e. when a PR is merged)

1. Same setup as CI
2. Re-runs Detekt + unit tests as a gate
3. Assembles the debug APK
4. Uploads it to **Firebase App Distribution**

The **debug** variant is distributed on purpose: release builds enable R8/minify but
the project has no ProGuard keep rules yet, so a minified release would risk runtime
crashes (kotlinx-serialization / Retrofit / Hilt). The debug APK is unminified and is
always signed with the debug keystore, so testers can install it directly.

## Required GitHub configuration

Add these under **Settings → Secrets and variables → Actions**.

### Secrets

| Name | Value |
| --- | --- |
| `TMDB_API_KEY` | TMDB API read-access token (the same value you keep in `local.properties`) |
| `FIREBASE_APP_ID` | Firebase Android App ID, e.g. `1:1234567890:android:abc123`. Must belong to a Firebase Android app registered with package name **`com.muhiqbal.moviedb.debug`** (the debug variant adds the `.debug` applicationId suffix) |
| `FIREBASE_SERVICE_ACCOUNT` | Full JSON of a Google Cloud service-account key (root has `"type": "service_account"`) with the **Firebase App Distribution Admin** role |

> `app/google-services.json` is committed to the repo, so CI does not need a secret for it.

### Variables (optional)

| Name | Default | Value |
| --- | --- | --- |
| `FIREBASE_TESTER_GROUPS` | `testers` | Comma-separated Firebase App Distribution tester-group aliases |

## One-time Firebase setup

1. In the Firebase console, add an Android app with package `com.muhiqbal.moviedb.debug`.
2. Enable **App Distribution** for the project and create a tester group (default alias `testers`).
3. Create a service account (IAM) with **Firebase App Distribution Admin**, download its JSON key, and paste the contents into the `FIREBASE_SERVICE_ACCOUNT` secret.
4. Copy the Android App ID into `FIREBASE_APP_ID`.

## Notes

- To distribute a signed **release** build instead, add a release `signingConfig` (keystore
  via secrets), add ProGuard keep rules, then switch the `file:` path in `deploy.yml` to
  `app/build/outputs/apk/release/app-release.apk` and register the Firebase app for
  package `com.muhiqbal.moviedb`.
