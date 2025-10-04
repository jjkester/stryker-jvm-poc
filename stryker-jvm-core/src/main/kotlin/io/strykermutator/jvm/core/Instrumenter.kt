package io.strykermutator.jvm.core

import io.strykermutator.jvm.language.MutantInstrumenter
import io.strykermutator.jvm.language.SequentialMutantIdGenerator
import io.strykermutator.jvm.runner.SourceRoot
import java.io.File

public interface Instrumenter {

    public fun instrument(sourceRoot: SourceRoot, target: SourceRoot)
}

public class DefaultInstrumenter(private val mutantInstrumenters: Set<MutantInstrumenter>) : Instrumenter {
    override fun instrument(
        sourceRoot: SourceRoot,
        target: SourceRoot
    ) {
        sourceRoot.file.walkTopDown()
            .filter { it.isFile }
            .forEach { sourceFile ->
                val targetFile = File(target.file, sourceFile.toRelativeString(sourceRoot.file))

                // TODO: Keep state about possible changing lines - this is ignored for now

                // TODO: Different error when none found (that's fine) vs. multiple found (not allowed)
                mutantInstrumenters.singleOrNull { it.supports(sourceFile) }
                    ?.instrument(sourceFile, targetFile, SequentialMutantIdGenerator())
            }
    }
}
