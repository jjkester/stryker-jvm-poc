plugins {
    buildsrc.convention.`kotlin-jvm-library`
}

dependencies {
    api(project(":stryker-jvm-common-api"))

    compileOnly(libs.kotlinx.coroutines.core)
}
