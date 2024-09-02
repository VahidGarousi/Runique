plugins {
    alias(libs.plugins.runique.android.library)
    alias(libs.plugins.runique.jvm.ktor)
}

android {
    namespace = "ir.runique.analytics.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.koin)
    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.analytics.domain)
}