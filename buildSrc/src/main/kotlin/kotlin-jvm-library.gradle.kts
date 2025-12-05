/*
 * Shared build logic for Kotlin JVM libraries.
 */

package buildsrc.convention

import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm")
    `maven-publish`
}

kotlin {
    jvmToolchain(17)

    /*
     * Enable explicit api mode.
     *
     * Requires production code to have an explicit 'public' visibility modifier, and requires public functions and
     * properties to have an explicit type specified. This limits the public API when using Kotlin, and ensures that
     * types in the public api are not changed inadvertently.
     */
    explicitApi()
}

tasks.withType<Test>().configureEach {
    // Configure all test Gradle tasks to use JUnitPlatform.
    useJUnitPlatform()

    // Log information about all test results, not only the failed ones.
    testLogging {
        events(
            TestLogEvent.FAILED,
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
        }
    }
}
