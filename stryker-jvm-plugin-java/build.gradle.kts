plugins {
    id("buildsrc.convention.kotlin-jvm")
}

dependencies {
    implementation(project(":stryker-jvm-language-api"))
    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.25.4")
    testImplementation(libs.test.junit)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.14.0")
}