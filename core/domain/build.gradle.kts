plugins {
    alias(libs.plugins.app.android.library)
}

android {
    namespace = "com.muhiqbal.moviedb.core.domain"
}

dependencies {
    implementation(libs.java.inject)
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
}