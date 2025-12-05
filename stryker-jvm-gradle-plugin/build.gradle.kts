plugins {
    buildsrc.convention.`kotlin-jvm-library`
    `java-gradle-plugin`
}

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        register("functionalTest", JvmTestSuite::class) {
            dependencies {
                implementation(gradleTestKit())
                implementation(libs.test.junit.jupiter)
                implementation(libs.test.assertk)
                runtimeOnly(libs.test.junit.launcher)
            }

            targets.configureEach {
                testTask {
                    systemProperty(
                        "testProjectDir",
                        sources.resources.sourceDirectories.singleFile.parentFile.resolve("testProjects")
                    )
                }
            }
        }
    }
}

@Suppress("UnstableApiUsage")
tasks.named("check") {
    dependsOn(testing.suites.named("functionalTest"))
}

gradlePlugin {
    testSourceSets(sourceSets["functionalTest"])

    plugins {
        create("stryker-jvm") {
            id = "io.strykermutator.jvm"
            implementationClass = "io.strykermutator.jvm.gradle.StrykerJvmGradlePlugin"
        }
    }
}

tasks.named<Test>("functionalTest") {

    // Publish all libraries to the local repository folder
    rootProject.subprojects.filter { it != project }.forEach { otherProject ->
        localRepository.findPublicationTask(otherProject)?.let { dependsOn(it) }
    }

    // Set environment variable to local repository directory for test projects
    environment("LOCAL_REPOSITORY_DIR", localRepository.absoluteUri)
}
