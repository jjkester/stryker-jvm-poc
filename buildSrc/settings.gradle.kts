dependencyResolutionManagement {
    // Ensure only repositories specified here are used.
    @Suppress("UnstableApiUsage")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    // Use Maven Central for all dependencies.
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }

    // Reuse the version catalog from the main build.
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "buildSrc"
