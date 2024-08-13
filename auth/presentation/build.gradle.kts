plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "ir.runique.auth.presentation"
    compileSdk = 34
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.auth.domain)
}