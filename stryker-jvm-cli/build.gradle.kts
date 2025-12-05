plugins {
    buildsrc.convention.`kotlin-jvm-library`
    application
}

dependencies {
    implementation(project(":stryker-jvm-core"))
    implementation(libs.clikt)
}

application {
    mainClass = "io.strykermutator.jvm.cli.StrykerJvmCli"
}
