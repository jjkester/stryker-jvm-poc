package io.strykermutator.jvm.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import io.strykermutator.jvm.core.LanguagePlugins

internal class Info : CliktCommand(name = "info") {

    val languagePlugins by option(names = arrayOf("--language-plugins")).flag(default = false)

    override fun help(context: Context): String = "Show info about the environment"

    override fun run() {
        if (languagePlugins) {
            echo("Language plugins: ${formattedLanguagePlugins() ?: "(none)"}")
        }
    }

    private fun formattedLanguagePlugins() = LanguagePlugins.load()
        .takeIf { it.isNotEmpty() }
        ?.joinToString { it.name }
}