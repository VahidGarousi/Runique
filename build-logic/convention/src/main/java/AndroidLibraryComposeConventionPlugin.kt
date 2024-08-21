import com.android.build.api.dsl.LibraryExtension
import ir.runique.buildlogic.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            pluginManager.run {
                apply("runique.android.library")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }
}