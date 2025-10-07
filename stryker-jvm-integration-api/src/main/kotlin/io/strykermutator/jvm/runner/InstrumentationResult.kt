package io.strykermutator.jvm.runner

import io.strykermutator.jvm.common.Mutant

/**
 * Result of instrumenting the [original sources][originalSources] with the generated [mutants].
 *
 * This result does not contain the mutated code. The mutated code is written to the
 * [instrumented source root][instrumentedSources], preserving file names and directory structure.
 */
public interface InstrumentationResult {

    /**
     * Source root containing the original source code.
     */
    public val originalSources: SourceRoot

    /**
     * Source root containing the instrumented source code.
     */
    public val instrumentedSources: SourceRoot

    /**
     * Placed mutants.
     */
    public val mutants: Set<Mutant>
}

public data class DefaultInstrumentationResult(
    override val originalSources: SourceRoot,
    override val instrumentedSources: SourceRoot,
    override val mutants: Set<Mutant>
) : InstrumentationResult
