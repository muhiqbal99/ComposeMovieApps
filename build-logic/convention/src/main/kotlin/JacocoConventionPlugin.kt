import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

class JacocoConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("jacoco")

        extensions.configure<JacocoPluginExtension> {
            toolVersion = "0.8.12"
        }

        pluginManager.withPlugin("com.android.library") {
            extensions.configure<LibraryExtension> {
                buildTypes.getByName("debug") {
                    enableUnitTestCoverage = true
                }
            }
        }

        tasks.register<JacocoReport>("jacocoViewModelReport") {
            group = "verification"
            description = "Generates JaCoCo coverage report for ViewModels only."
            dependsOn("testDebugUnitTest")

            reports {
                xml.required.set(true)
                csv.required.set(true)
                html.required.set(true)
            }

            val viewModelClasses = listOf(
                "**/*ViewModel.class",
                "**/*ViewModel\$*.class",
            )
            val classDirs = listOf(
                "intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes",
                "tmp/kotlin-classes/debug",
                "intermediates/javac/debug/classes",
            )
            classDirectories.setFrom(
                classDirs.map { dir ->
                    fileTree(layout.buildDirectory.dir(dir)) { include(viewModelClasses) }
                },
            )
            sourceDirectories.setFrom(files("src/main/kotlin"))
            executionData.setFrom(
                fileTree(layout.buildDirectory) { include("**/testDebugUnitTest.exec") },
            )
        }

        tasks.withType<JacocoReport>().configureEach {
            mustRunAfter(tasks.matching { it.name == "testDebugUnitTest" })
        }
    }
}
