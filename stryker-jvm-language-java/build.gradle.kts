plugins {
    buildsrc.convention.`kotlin-jvm-library`
}

dependencies {
    implementation(project(":stryker-jvm-language-api"))
    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.25.4")
    testImplementation(libs.test.junit.jupiter)
    testRuntimeOnly(libs.test.junit.launcher)
}
