package ir.runique.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

fun DependencyHandlerScope.addUiLayerDependencies(
    project: Project
) {
    add(configurationName = "implementation", dependencyNotation = project(":core:presentation:ui"))
    add(
        configurationName = "implementation",
        dependencyNotation = project(":core:presentation:designsystem")
    )
    add(
        configurationName = "implementation",
        dependencyNotation = project.libs.findBundle("koin-compose").get()
    )
    add(
        configurationName = "implementation",
        dependencyNotation = project.libs.findBundle("compose").get()
    )
    add(
        configurationName = "debugImplementation",
        dependencyNotation = project.libs.findBundle("compose-debug").get()
    )
    add(
        configurationName = "androidTestImplementation",
        dependencyNotation = project.libs.findLibrary("androidx.compose.ui.test.junit4").get()
    )
}