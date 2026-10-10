package io.strykermutator.jvm.gradle.task

import io.strykermutator.jvm.gradle.MutateSpec
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceSet

/**
 * Task to introduce mutants to sources.
 */
internal abstract class MutateTask : JavaExec(), MutateSpec {

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

    init {
        // Sets the default main class if none is specified
        mainClass.convention(MAIN_CLASS)
    }

    override fun exec() {
        sources.finalizeValue()
        target.finalizeValue()

        // Run Stryker JVM CLI
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

        super.exec()
    }

    internal companion object {

        const val MAIN_CLASS = "io.strykermutator.jvm.cli.StrykerJvmCli"
        const val MUTATE_COMMAND = "mutate"
        const val TASK_NAME_VERB = "mutate"

        fun name(sourceSet: SourceSet): String = sourceSet.getTaskName(TASK_NAME_VERB, null)
    }
}
