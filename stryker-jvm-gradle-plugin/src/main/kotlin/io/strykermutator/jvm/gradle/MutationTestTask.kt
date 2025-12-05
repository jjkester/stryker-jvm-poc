package io.strykermutator.jvm.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.SourceSet

public abstract class MutationTestTask : DefaultTask(), MutateSpec {

    @get:Input
    public abstract val sourceSet: Property<SourceSet>

    @get:Input
    public abstract val pluginConfiguration: Property<Configuration>

    internal companion object {

        const val TASK_NAME_VERB = "mutationTest"

        fun name(sourceSet: SourceSet): String = sourceSet.getTaskName(TASK_NAME_VERB, null)
    }
}
