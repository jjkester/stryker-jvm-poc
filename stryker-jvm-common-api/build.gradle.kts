plugins {
    buildsrc.convention.`kotlin-jvm-library`
    kotlin("plugin.serialization") version "2.3.20"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}