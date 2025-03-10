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

rootProject.name = "stryker-jvm"
