@file:JvmName("StrykerJvmCli")

package io.strykermutator.jvm.cli

import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.output.MordantHelpFormatter
import com.github.ajalt.clikt.parameters.options.versionOption
import io.strykermutator.jvm.cli.commands.Info
import io.strykermutator.jvm.cli.commands.Mutate

internal class Root : NoOpCliktCommand(name = "stryker") {

    init {
        versionOption(this::class.java.`package`.implementationVersion ?: "unknown")

        context {
            helpFormatter = { MordantHelpFormatter(it, showDefaultValues = true) }
        }
    }
}

public fun main(args: Array<String>): Unit = Root()
    .subcommands(Info(), Mutate())
    .main(args)
