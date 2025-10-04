package io.strykermutator.jvm.core

import io.strykermutator.jvm.common.DefaultMutatableFile
import io.strykermutator.jvm.common.MutatableFile
import io.strykermutator.jvm.language.LanguagePlugin
import io.strykermutator.jvm.language.SequentialMutantIdGenerator
import io.strykermutator.jvm.runner.SourceRoot
import java.io.File

public interface Instrumenter {

    public fun instrument(source: SourceRoot, target: SourceRoot)
}

public class DefaultInstrumenter(private val languagePlugins: Set<LanguagePlugin>) : Instrumenter {
    override fun instrument(
        source: SourceRoot,
        target: SourceRoot
    ) {
        source.file.walkTopDown()
            .filter { it.isFile }
            .map { it.toMutatableFile(source, target) }
            .forEach { file ->
                // TODO: Different error when none found (that's fine) vs. multiple found (not allowed)
                languagePlugins.singleOrNull { it.supports(file) }
                    ?.instrument(file, SequentialMutantIdGenerator())
            }
    }

    private fun File.toMutatableFile(source: SourceRoot, target: SourceRoot): MutatableFile =
        DefaultMutatableFile(this, File(target.file, toRelativeString(source.file)))
}
