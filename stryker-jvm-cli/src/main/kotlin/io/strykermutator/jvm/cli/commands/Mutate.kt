package io.strykermutator.jvm.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.ProcessedArgument
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.defaultLazy
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import io.strykermutator.jvm.common.StringCompanionMethodRef
import io.strykermutator.jvm.core.DefaultInstrumenter
import io.strykermutator.jvm.core.Instrumenter
import io.strykermutator.jvm.core.LanguagePlugins
import io.strykermutator.jvm.language.DefaultMutationConfiguration
import io.strykermutator.jvm.language.MutantIdGenerator
import io.strykermutator.jvm.runner.DefaultSourceRoot
import java.nio.file.Paths

internal class Mutate(
    private val instrumenterFactory: InstrumenterFactory = defaultInstrumenterFactory
) : CliktCommand(name = "mutate") {

    val source by argument(name = "source", help = "Root directory of the source code to mutate")
        .sourceDir()
        .defaultLazy(defaultForHelp = "Current directory") {
            Paths.get("").toAbsolutePath()
        }

    val target by argument(name = "target", help = "Target directory for the mutated sources")
        .targetDir()
        .defaultLazy(defaultForHelp = "Source directory") { source }

    val companionClass by option(
        names = arrayOf("--companion-class"),
        help = "Fully qualified class name of the Stryker Companion class"
    )
        .default(value = "io.strykermutator.jvm.companion.StrykerCompanion")

    val companionMethod by option(
        names = arrayOf("--companion-method"),
        help = "Name of the method on the Stryker Companion class"
    )
        .default(value = "mutantActive")

    override fun help(context: Context): String = "Instrument source files with mutants"

    override fun run() {
        instrumenterFactory.create(LanguagePlugins.load()).instrument(
            source = DefaultSourceRoot(source.toFile()),
            target = DefaultSourceRoot(target.toFile()),
            configuration = DefaultMutationConfiguration(
                companionMethodRef = StringCompanionMethodRef(companionClass, companionMethod),
                mutantIdGenerator = MutantIdGenerator.sequential()
            )
        )
    }

    private companion object {

        val defaultInstrumenterFactory = InstrumenterFactory { languagePlugins ->
            DefaultInstrumenter(languagePlugins)
        }

        private fun ProcessedArgument<String, String>.sourceDir() = path(
            mustExist = true,
            canBeFile = false,
            canBeDir = true,
            mustBeWritable = false,
            mustBeReadable = true
        )

        private fun ProcessedArgument<String, String>.targetDir() = path(
            mustExist = false,
            canBeFile = false,
            canBeDir = true,
            mustBeWritable = true,
            mustBeReadable = true
        )
    }

    internal fun interface InstrumenterFactory {

        fun create(languagePlugins: LanguagePlugins): Instrumenter
    }
}
