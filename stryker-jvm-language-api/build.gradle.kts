plugins {
    id("buildsrc.convention.kotlin-jvm")
}

dependencies {
    api(project(":stryker-jvm-common-api"))

    implementation(libs.kotlinx.coroutines.core)
}
