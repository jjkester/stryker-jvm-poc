plugins {
    buildsrc.convention.`kotlin-jvm-library`
    kotlin("plugin.serialization") version "2.3.20"
}

dependencies {
    implementation(project(":stryker-jvm-core"))
    implementation(libs.java.javaparser)
    implementation(libs.kotlinx.coroutines.core)
    implementation("org.apache.maven.shared:maven-invoker:3.3.0")
    implementation(libs.kotlinx.serialization.json)
    runtimeOnly(project(":stryker-jvm-language-java"))

    testImplementation(libs.test.junit.jupiter)
    testRuntimeOnly(libs.test.junit.launcher)
}
