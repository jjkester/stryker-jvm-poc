package io.strykermutator.jvm.core

import io.strykermutator.jvm.common.DefaultMutatableFile
import io.strykermutator.jvm.common.MutatableFile
import io.strykermutator.jvm.language.MutationConfiguration
import io.strykermutator.jvm.runner.SourceRoot
import java.io.File

public interface Instrumenter {

    public fun instrument(source: SourceRoot, target: SourceRoot, configuration: MutationConfiguration)
}

public class DefaultInstrumenter(private val languagePlugins: LanguagePlugins) : Instrumenter {
    override fun instrument(
        source: SourceRoot,
        target: SourceRoot,
        configuration: MutationConfiguration
    ) {
        source.file.walkTopDown()
            .filter { it.isFile }
            .map { it.toMutatableFile(source, target) }
            .forEach { file ->
                // TODO: Different error when none found (that's fine) vs. multiple found (not allowed)
                languagePlugins[file]
                    ?.instrument(file, configuration)
            }
    }

    private fun File.toMutatableFile(source: SourceRoot, target: SourceRoot): MutatableFile =
        DefaultMutatableFile(this, File(target.file, toRelativeString(source.file)))
}
