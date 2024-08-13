plugins {
    alias(libs.plugins.runique.android.library)
}

android {
    namespace = "ir.runique.run.network"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
}