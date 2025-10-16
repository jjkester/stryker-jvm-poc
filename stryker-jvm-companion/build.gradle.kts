plugins {
    id("buildsrc.convention.kotlin-jvm")
    `maven-publish`
}

group = "io.strykermutator"
version = "0.1.0-SNAPSHOT"


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()   // Publishes to ~/.m2/repository
    }
}