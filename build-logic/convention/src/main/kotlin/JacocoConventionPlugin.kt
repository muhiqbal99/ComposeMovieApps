import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension

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
    }
}
