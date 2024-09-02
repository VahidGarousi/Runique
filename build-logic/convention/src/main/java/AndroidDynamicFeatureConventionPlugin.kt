import com.android.build.api.dsl.DynamicFeatureExtension
import ir.runique.buildlogic.ExtensionType
import ir.runique.buildlogic.addUiLayerDependencies
import ir.runique.buildlogic.configureAndroidCompose
import ir.runique.buildlogic.configureBuildTypes
import ir.runique.buildlogic.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

internal class AndroidDynamicFeatureConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            pluginManager.run {
                apply("com.android.dynamic-feature")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            extensions.configure<DynamicFeatureExtension> {
                configureKotlinAndroid(this)
                configureAndroidCompose(this)
                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.DYNAMIC_FEATURE
                )
            }
            dependencies {
                addUiLayerDependencies(project)
                "testImplementation"(kotlin("test"))
            }
        }
    }
}