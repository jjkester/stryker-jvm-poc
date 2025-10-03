plugins {
    id("buildsrc.convention.kotlin-jvm")
}

dependencies {
    implementation(project(":stryker-jvm-integration-api"))
    implementation(project(":stryker-jvm-language-api"))
}
