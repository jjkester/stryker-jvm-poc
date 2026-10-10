package io.strykermutator.jvm.gradle.tasks

import org.gradle.api.tasks.JavaExec

/**
 * Task to run Stryker JVM CLI commands.
 */
internal abstract class CliTask : JavaExec() {

    init {
        // Sets the default main class if none is specified
        mainClass.convention(MAIN_CLASS)
    }

    open fun beforeExec() {}

    final override fun exec() {
        beforeExec()
        super.exec()
    }

    internal companion object {
        const val MAIN_CLASS = "io.strykermutator.jvm.cli.StrykerJvmCli"
    }
}
