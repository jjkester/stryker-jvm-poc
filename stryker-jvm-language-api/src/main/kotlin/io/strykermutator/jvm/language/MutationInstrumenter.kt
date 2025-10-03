package io.strykermutator.jvm.language

import java.io.File

/**
 * Stryker JVM mutant generator.
 *
 * Mutant generators are responsible for analyzing a file and determining the relevant mutants for that file.
 */
public interface MutantGenerator {

    /**
     * Determines whether a file is supported by this mutant generator.
     */
    public fun supports(file: File): Boolean

    /**
     * Generates the relevant mutants for this file. Returns a set of mutants to be applied to the file.
     *
     * Mutants must have a unique id within the scope of this file and this mutant generator.
     */
    public fun generate(file: File): Set<Mutant>
}