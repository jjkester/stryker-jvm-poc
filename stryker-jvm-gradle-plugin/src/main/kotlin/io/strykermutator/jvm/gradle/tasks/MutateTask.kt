package io.strykermutator.jvm.gradle.tasks

import io.strykermutator.jvm.gradle.MutateSpec
import io.strykermutator.jvm.gradle.util.taskName
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory

/**
 * Task to introduce mutants to sources.
 */
internal abstract class MutateTask : CliTask(), MutateSpec {

    /**
     * Sources to mutate.
     */
    @get:InputFiles
    abstract val sources: Property<SourceDirectorySet>

    /**
     * Target directory to place the mutated sources. The original directory structure is retained.
     */
    @get:OutputDirectory
    abstract val target: DirectoryProperty

    override fun beforeExec() {
        sources.finalizeValue()
        target.finalizeValue()

        args(
            listOfNotNull(
                MUTATE_COMMAND,
                companionClass.map { "--companion-class=$it" }.orNull,
                companionMethod.map { "--companion-method=$it" }.orNull,
                *sources.map { source -> source.sourceDirectories.files.filter { it.exists() } }
                    .getOrElse(emptyList())
                    .toTypedArray(),
                target.get()
            )
        )
    }

    internal companion object {

        private const val MUTATE_COMMAND = "mutate"
        private const val TASK_NAME_VERB = "mutate"

        fun name(mutationTestName: String): String = taskName(TASK_NAME_VERB, mutationTestName)
    }
}
