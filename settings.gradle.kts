dependencyResolutionManagement {
    // Ensure only repositories specified here are used.
    @Suppress("UnstableApiUsage")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    // Use Maven Central for all dependencies.
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

// Included subprojects (in alphabetical order).
include(":stryker-jvm-cli")
include(":stryker-jvm-common-api")
include(":stryker-jvm-companion")
include(":stryker-jvm-core")
include(":stryker-jvm-gradle-plugin")
include(":stryker-jvm-integration-api")
include(":stryker-jvm-language-api")
include(":stryker-jvm-language-java")
include(":stryker-jvm-runner")

rootProject.name = "stryker-jvm"
