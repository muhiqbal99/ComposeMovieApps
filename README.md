# MovieApp

![ViewModel coverage](.github/badges/jacoco.svg)

A native Android movie browser built with Kotlin and Jetpack Compose, backed by the
[TMDB API](https://www.themoviedb.org/). Browse movies by genre, discover popular
titles, search, and view rich detail pages with trailers, cast, and reviews.

The project is a multi-module, MVVM, wired with Hilt, Paging 3,
Retrofit, and Navigation 3, plus Detekt static analysis and a GitHub Actions CI/CD
pipeline that auto-distributes builds to Firebase App Distribution.

---

## Features

- **Browse** – genres rendered as horizontal movie carousels, each with a "See More"
  shortcut into the full genre list.
- **Discover** – a 2-column grid of popular movies (Paging 3, infinite scroll).
- **Search** – debounced movie search.
- **Movie detail** – backdrop hero, YouTube trailer playback, cast & crew, paged reviews,
  and a Detail/Reviews tab layout.
- **Localized** – English and Bahasa Indonesia, for both UI strings *and* TMDB API
  responses (the request language follows the device locale).

---

## Tech stack

| Area | Choice |
| --- | --- |
| Language | Kotlin 2.2.21 |
| UI | Jetpack Compose (BOM 2026.01.01), Material 3 |
| Architecture | MVVM + unidirectional state, multi-module |
| DI | Hilt 2.59.2 (KSP) |
| Navigation | Navigation 3 (`1.0.0`) — per-tab back stacks |
| Paging | Paging 3 (`3.3.6`) |
| Networking | Retrofit 3 + OkHttp + kotlinx.serialization |
| Images | Coil |
| Async | Coroutines / Flow |
| Build | AGP 9.0.1, Gradle version catalog, `build-logic` convention plugins |
| Static analysis | Detekt (`2.0.0-alpha.1`) with Compose rules |
| Testing | JUnit, Truth, MockK, Turbine, Coroutines-test, Paging-test, Robolectric |
| CI/CD | GitHub Actions → Firebase App Distribution |

- `compileSdk` / `targetSdk` **36**, `minSdk` **24**
- Application ID `com.muhiqbal.moviedb` (debug variant: `com.muhiqbal.moviedb.debug`)

---

## Module structure

```
:app                     # entry point, bottom-nav scaffold, DI wiring
:core
  :common                # shared utilities, error types (NoConnectivityException)
  :domain                # models + repository interfaces (pure Kotlin)
  :data                  # repository impls, Paging sources, DTO→domain mappers
  :network               # Retrofit service, OkHttp, interceptors (auth, language)
  :ui                    # theme, design tokens, shared Compose helpers
  :testing               # fakes shared across module tests
:feature
  :genre                 # Browse tab — genre carousels
  :movie                 # Discover / Search / Movie detail
```

Dependencies flow one way: `feature` → `core:ui`/`core:domain` → `core:data` →
`core:network`. Domain is pure Kotlin and has no Android dependencies.

Cross-cutting Gradle config (Android, Compose, Hilt, Detekt, lint) lives in
`build-logic/` convention plugins so module build files stay tiny.

---

## Getting started

1. Clone and open in Android Studio.
2. Add your TMDB API read token to `local.properties`:
   ```properties
   TMDB_API_KEY=your_tmdb_v4_read_access_token
   ```
3. Add a Firebase Android app (package `com.muhiqbal.moviedb.debug`) and drop its
   `app/google-services.json` in place.
4. Run the `app` configuration.

### Useful commands

```bash
./gradlew :app:assembleDebug      # build the debug APK
./gradlew testDebugUnitTest       # run all unit tests (incl. Robolectric UI tests)
./gradlew detekt                  # static analysis
./gradlew detektBaseline          # regenerate Detekt baselines
```

---

## Testing

- **Unit / ViewModel tests** with JUnit, Truth, MockK, Turbine and Coroutines-test.
- **Compose UI tests** run on the JVM via **Robolectric** (no emulator), which keeps them
  fast and reliable in CI and avoids the Espresso `InputManager` incompatibility on the
  newest emulator system images.
- Shared fakes live in `:core:testing`.

### Code coverage (JaCoCo)

Coverage is intentionally scoped to **ViewModels only** — the layer holding presentation
logic worth asserting — via the `app.jacoco` convention plugin. Generate it with:

```bash
./gradlew jacocoViewModelReport
```

Per-module HTML/XML/CSV reports land in
`feature/<module>/build/reports/jacoco/jacocoViewModelReport/`. The badge at the top of
this README is regenerated and committed automatically on every push to `main` by
[`coverage.yml`](.github/workflows/coverage.yml).

---

## Static analysis — Detekt

Detekt is applied to every module through the `app.android.detekt` convention plugin,
configured from `config/detekt/detekt.yml` with the Compose rule set. Pre-existing
findings are captured in per-module `detekt-baseline.xml` files, so `./gradlew detekt`
passes today while still failing the build on any *new* violation.

---

## CI/CD

Two GitHub Actions workflows (`.github/workflows/`):

- **`ci.yml`** — on every pull request to `main`: Detekt → unit tests → `assembleDebug`,
  with reports uploaded as an artifact.
- **`deploy.yml`** — on merge to `main`: re-verifies, builds the debug APK, then uploads
  it to **Firebase App Distribution** (via `google-github-actions/auth` + the Firebase
  CLI). The **debug** variant is distributed because release builds enable R8 and the
  project has no keep rules yet.

See [`docs/ci-cd.md`](docs/ci-cd.md) for the full setup, including the required
repository secrets (`TMDB_API_KEY`, `FIREBASE_APP_ID`, `FIREBASE_SERVICE_ACCOUNT`).

---

## License

This project is for assessment / educational purposes. Movie data and images are
provided by TMDB; this product uses the TMDB API but is not endorsed or certified by TMDB.
