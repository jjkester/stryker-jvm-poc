package io.strykermutator.jvm.gradle.util

internal fun taskName(verb: String, mutationTestName: String): String =
    "$verb${mutationTestName.replaceFirstChar { it.uppercaseChar() }}"
