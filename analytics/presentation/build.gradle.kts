plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "ir.runique.analytics.presentation"
}

dependencies {
    implementation(projects.analytics.domain)
}