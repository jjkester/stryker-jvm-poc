package io.strykermutator.jvm.core

import io.strykermutator.jvm.language.MutantGenerator
import io.strykermutator.jvm.runner.SourceRoot
import java.io.File

public interface Instrumenter {

    public fun instrument(sourceRoot: SourceRoot, target: SourceRoot)
}

public class DefaultInstrumenter(private val mutantGenerators: Set<MutantGenerator>) : Instrumenter {
    override fun instrument(
        sourceRoot: SourceRoot,
        target: SourceRoot
    ) {
        sourceRoot.file.walkTopDown()
            .filter { it.isFile }
            .associate { file ->
            val relativePath = file.relativeTo(sourceRoot.file).path
            val targetPath = File(target.file, relativePath)

            // TODO: Keep state about possible changing lines - this is ignored for now

            val mutants = mutantGenerators
                .asSequence()
                .filter { it.supports(file) }
                .flatMap { it.generate(file) }
                .toSet()

            // TODO: This is extremely inefficient
            val fileContents = file.readLines()

            mutants.forEach { mutant ->
                // TODO: We do not support multi-line for now
                fileContents[mutant.location.start.line].replaceRange(mutant.location.start.column..mutant.location.endInclusive.column, mutant.guardedReplacement)
            }

            targetPath.writeText(fileContents.joinToString(System.lineSeparator()))

            file to mutants
        }
    }
}
