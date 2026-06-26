import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.gradleVersionsPlugin) apply false
    alias(libs.plugins.firebase) apply false
    jacoco
}

jacoco {
    toolVersion = "0.8.12"
}

private val jacocoModules = listOf(":feature:genre", ":feature:movie")

tasks.register<JacocoReport>("jacocoViewModelReport") {
    group = "verification"
    description = "Unified ViewModel-only JaCoCo coverage report across all feature modules."

    val coveredProjects = jacocoModules.map { project(it) }
    dependsOn(coveredProjects.map { "${it.path}:testDebugUnitTest" })

    // ViewModel classes only. Synthetic coroutine continuations / lambda classes
    // (e.g. *ViewModel$loadX$1) are excluded: their "branches" are compiler-generated
    // suspend state-machine dispatch, not real logic, and are not meaningfully coverable.
    val viewModelClasses = listOf("**/*ViewModel.class")
    val classDirNames = listOf(
        "intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes",
        "tmp/kotlin-classes/debug",
        "intermediates/javac/debug/classes",
    )

    classDirectories.setFrom(
        files(
            coveredProjects.flatMap { p ->
                classDirNames.map { dir ->
                    p.fileTree(p.layout.buildDirectory.dir(dir).get().asFile) { include(viewModelClasses) }
                }
            },
        ),
    )
    sourceDirectories.setFrom(
        files(coveredProjects.map { it.layout.projectDirectory.dir("src/main/kotlin").asFile }),
    )
    executionData.setFrom(
        files(
            coveredProjects.map { p ->
                p.fileTree(p.layout.buildDirectory.get().asFile) { include("**/testDebugUnitTest.exec") }
            },
        ),
    )

    reports {
        xml.required.set(true)
        csv.required.set(true)
        html.required.set(true)
    }
}
