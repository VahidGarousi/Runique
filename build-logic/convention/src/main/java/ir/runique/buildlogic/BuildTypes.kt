package ir.runique.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.run {
        buildFeatures {
            buildConfig = true
        }
        val apiKey = gradleLocalProperties(
            projectRootDir = rootDir,
            providers = providers
        ).getProperty("API_KEY")

        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey = apiKey)
                        }

                        release {
                            configureReleaseBuildType(
                                commonExtension = commonExtension,
                                apiKey = apiKey
                            )
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey = apiKey)
                        }

                        release {
                            configureReleaseBuildType(
                                commonExtension = commonExtension,
                                apiKey = apiKey
                            )
                        }
                    }
                }
            }

            ExtensionType.DYNAMIC_FEATURE -> {
                extensions.configure<DynamicFeatureExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey = apiKey)
                        }

                        release {
                            configureReleaseBuildType(
                                commonExtension = commonExtension,
                                apiKey = apiKey
                            )
                        }
                    }
                }
            }
        }
    }
}


private fun BuildType.configureDebugBuildType(
    apiKey: String
) {
    buildConfigField(
        type = "String",
        name = "API_KEY",
        value = "\"$apiKey\""
    )
    buildConfigField(
        type = "String",
        name = "BASE_URL",
        value = "\"https://runique.pl-coding.com:8080\""
    )
}


private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    apiKey: String
) {
    buildConfigField(
        type = "String",
        name = "API_KEY",
        value = "\"$apiKey\""
    )
    buildConfigField(
        type = "String",
        name = "BASE_URL",
        value = "\"https://runique.pl-coding.com:8080\""
    )


    isMinifyEnabled = true
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}