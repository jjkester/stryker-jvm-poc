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
include(":stryker-jvm-agent")
include(":stryker-jvm-integration-api")

rootProject.name = "stryker-jvm"
