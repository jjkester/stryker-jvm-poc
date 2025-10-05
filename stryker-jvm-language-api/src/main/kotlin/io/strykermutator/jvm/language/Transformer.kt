package io.strykermutator.jvm.language

import io.strykermutator.jvm.common.Mutant
import io.strykermutator.jvm.common.MutatableFile

/**
 * A transformer analyzes a source file, [generates][io.strykermutator.jvm.language.operator.MutationOperator] and
 * [places][io.strykermutator.jvm.language.placer.MutantPlacer] mutants, and writes the mutated sources to the target
 * file.
 *
 * Transformers are typically implemented for a single language or a single toolkit supporting multiple languages.
 */
public interface Transformer {

    /**
     * Transforms the source file to the target file by mutating the code.
     *
     * @param file source file to mutate and target file to write to.
     * @param configuration configuration of the mutation process.
     * @return set of generated and placed mutants.
     */
    public fun transform(file: MutatableFile, configuration: MutationConfiguration): Set<Mutant>
}