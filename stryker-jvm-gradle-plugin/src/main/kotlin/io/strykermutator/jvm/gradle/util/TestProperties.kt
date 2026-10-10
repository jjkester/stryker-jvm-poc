package io.strykermutator.jvm.gradle.util

import org.gradle.api.tasks.testing.Test

internal fun Test.configureFrom(other: Test) {
    testFrameworkProperty.set(other.testFrameworkProperty)
}
