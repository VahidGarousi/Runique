import ir.runique.buildlogic.addUiLayerDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureUiConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            pluginManager.run {
                apply("runique.android.library.compose")
            }
            dependencies {
                addUiLayerDependencies(project = project)
            }
        }
    }
}