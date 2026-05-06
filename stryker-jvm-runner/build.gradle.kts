plugins {
    buildsrc.convention.`kotlin-jvm-library`
}

dependencies {
    implementation(project(":stryker-jvm-core"))
    implementation(libs.java.javaparser)
    implementation(libs.kotlinx.coroutines.core)
    implementation("org.apache.maven.shared:maven-invoker:3.3.0")

    runtimeOnly(project(":stryker-jvm-language-java"))

    testImplementation(libs.test.junit.jupiter)
    testRuntimeOnly(libs.test.junit.launcher)
}
