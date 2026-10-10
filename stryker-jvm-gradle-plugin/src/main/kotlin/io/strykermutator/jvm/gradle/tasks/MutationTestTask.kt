package io.strykermutator.jvm.gradle.tasks

import io.strykermutator.jvm.gradle.MutateSpec
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.testing.Test

/**
 * Top-level task for a mutation test run of a single source set.
 */
public abstract class MutationTestTask : DefaultTask(), MutateSpec {

    /**
     * Source set containing the sources to mutate for testing.
     */
    @get:Input
    public abstract val sourceSet: Property<SourceSet>

    /**
     * Source set containing the tests to run.
     */
    @get:Input
    public abstract val testSourceSet: Property<SourceSet>

    /**
     * Test task to run the tests normally.
     */
    @get:Input
    public abstract val testTask: Property<Test>

    /**
     * Configuration containing the plugins to use for mutation.
     */
    @get:Input
    public abstract val pluginConfiguration: Property<Configuration>

    /**
     * Configuration containing the companion code to use for mutation.
     */
    @get:Input
    public abstract val companionConfiguration: Property<Configuration>

    internal companion object {

        const val TASK_NAME_VERB = "mutationTest"

        fun name(sourceSet: SourceSet): String = sourceSet.getTaskName(TASK_NAME_VERB, null)
    }
}
