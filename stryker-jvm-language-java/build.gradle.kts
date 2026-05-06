plugins {
    buildsrc.convention.`kotlin-jvm-library`
}

dependencies {
    implementation(project(":stryker-jvm-language-api"))
    implementation(libs.java.javaparser)
    testImplementation(libs.test.junit.jupiter)
    testRuntimeOnly(libs.test.junit.launcher)
}
