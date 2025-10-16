plugins {
    id("buildsrc.convention.kotlin-jvm")
}

dependencies {
    implementation(project(":stryker-jvm-core"))
    runtimeOnly(project(":stryker-jvm-language-java"))
    testImplementation(libs.test.junit)
    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.25.4")
    implementation("org.junit.platform:junit-platform-launcher:1.14.0")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.apache.maven.shared:maven-invoker:3.3.0")

}