package io.strykermutator.jvm.language

import io.strykermutator.jvm.common.Mutant
import io.strykermutator.jvm.common.MutatableFile

/**
 * Stryker JVM language plugin interface.
 *
 * Language plugins provide the ability to mutate programming languages to Stryker JVM.
 */
public interface LanguagePlugin {

    /**
     * Name of the language supported by this plugin. Names must follow common language names used for syntax
     * highlighting. This is typically the file extension.
     */
    public val name: String

    /**
     * Determines whether a mutatable file is supported by this plugin.
     */
    public fun supports(file: MutatableFile): Boolean

    /**
     * Generates the relevant mutants for the source file, and places the mutants in the target file.
     *
     * Mutants must have a unique id within the scope of this file and this mutant generator.
     */
    public fun instrument(file: MutatableFile, mutantIdGenerator: MutantIdGenerator): Set<Mutant>
}