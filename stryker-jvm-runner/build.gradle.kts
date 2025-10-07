plugins {
    id("buildsrc.convention.kotlin-jvm")
}

dependencies {
    implementation(project(":stryker-jvm-core"))
    runtimeOnly(project(":stryker-jvm-language-java"))
    testImplementation(libs.test.junit)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.14.0")
}