plugins {
    buildsrc.convention.`kotlin-jvm`
    application
}

dependencies {
    implementation(project(":stryker-jvm-core"))
    implementation(libs.clikt)
    runtimeOnly(project(":stryker-jvm-language-java")) // TODO: Should not be here...
}

application {
    mainClass = "io.strykermutator.jvm.cli.StrykerJvmCli"
}
