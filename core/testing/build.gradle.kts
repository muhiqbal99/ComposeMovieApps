plugins {
    alias(libs.plugins.app.android.library)
}

android {
    namespace = "com.muhiqbal.moviedb.core.testing"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.junit)
    implementation(libs.turbine)
    implementation(libs.truth)
    implementation(libs.kotlinx.coroutines.test)
}
