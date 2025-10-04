plugins {
    id("buildsrc.convention.kotlin-jvm")
}

dependencies {
    implementation(project(":stryker-jvm-core"))
    implementation(project(":stryker-jvm-plugin-java"))
    api(project(":stryker-jvm-language-api"))
    api(project(":stryker-jvm-integration-api"))
    testImplementation(libs.test.junit)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.14.0")
}