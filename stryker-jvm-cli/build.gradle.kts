plugins {
    buildsrc.convention.`kotlin-jvm-library`
    application
    kotlin("plugin.serialization") version "2.3.20"
}

dependencies {
    implementation(project(":stryker-jvm-core"))
    implementation(libs.clikt)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.test.assertk)
    testImplementation(libs.test.junit.launcher)
    testImplementation(libs.test.junit.jupiter)
}

application {
    mainClass = "io.strykermutator.jvm.cli.StrykerJvmCli"
}
