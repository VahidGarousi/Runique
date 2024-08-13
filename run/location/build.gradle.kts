plugins {
    alias(libs.plugins.runique.android.library)
}

android {
    namespace = "ir.runique.run.location"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.google.maps.android.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.timber)

    implementation(projects.core.domain)
    implementation(projects.run.domain)
}