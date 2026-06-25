plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.gradleVersionsPlugin) apply false
    alias(libs.plugins.firebase) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

tasks.register("jacocoViewModelReport") {
    group = "verification"
    description = "Runs the ViewModel-only JaCoCo report for every feature module."
    dependsOn(
        ":feature:genre:jacocoViewModelReport",
        ":feature:movie:jacocoViewModelReport",
    )
}