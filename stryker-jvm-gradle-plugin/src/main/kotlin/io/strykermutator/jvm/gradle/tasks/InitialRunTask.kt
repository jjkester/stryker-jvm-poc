package io.strykermutator.jvm.gradle.tasks

import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.testing.Test

/**
 * Task to run the tests against the mutated sources for the first time, providing test result and coverage information.
 */
internal abstract class InitialRunTask : Test() {

    /**
     * Source set containing the tests to run.
     */
    @get:Internal
    abstract val testSourceSet: Property<SourceSet>

    /**
     * Source set containing the code before mutation.
     */
    @get:Internal
    abstract val originalSourceSet: Property<SourceSet>

    /**
     * Source set containing the mutated code.
     */
    @get:Internal
    abstract val mutatedSourceSet: Property<SourceSet>

    /**
     * Classpath of the companion.
     */
    @get:InputFiles
    abstract val companionClasspath: Property<FileCollection>

    init {
        // Set test classes from the test source set
        testClassesDirs = project.files(testSourceSet.map { it.output.classesDirs })

        // Set classpath from test source set, replacing the original code with the mutated code
        classpath = project.files(testSourceSet.map { it.runtimeClasspath }) -
                project.files(originalSourceSet.map { it.runtimeClasspath }) +
                project.files(mutatedSourceSet.map { it.runtimeClasspath }) +
                project.files(companionClasspath)

        // Configure reporting
        reports.html.required.set(false)
        reports.junitXml.required.set(true)
    }

    companion object {

        const val TASK_NAME_VERB = "initialRun"

        fun name(mutatedSourceSet: SourceSet): String = mutatedSourceSet.getTaskName(TASK_NAME_VERB, null)
    }
}
