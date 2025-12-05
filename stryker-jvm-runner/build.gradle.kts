plugins {
    buildsrc.convention.`kotlin-jvm-library`
}

dependencies {
    implementation(project(":stryker-jvm-core"))
    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.25.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.apache.maven.shared:maven-invoker:3.3.0")

    runtimeOnly(project(":stryker-jvm-language-java"))

    testImplementation(libs.test.junit.jupiter)
    testRuntimeOnly(libs.test.junit.launcher)
}
