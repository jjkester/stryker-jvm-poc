plugins {
    id("buildsrc.convention.kotlin-jvm")
}

dependencies {
    compileOnly(project(":stryker-jvm-companion"))
    api(project(":stryker-jvm-integration-api"))
    implementation(project(":stryker-jvm-language-api"))
}
