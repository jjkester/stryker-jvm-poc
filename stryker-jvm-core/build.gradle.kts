plugins {
    buildsrc.convention.`kotlin-jvm-library`
}

dependencies {
    compileOnly(project(":stryker-jvm-companion"))
    api(project(":stryker-jvm-integration-api"))
    api(project(":stryker-jvm-language-api"))
}
