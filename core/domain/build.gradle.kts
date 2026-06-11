plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
