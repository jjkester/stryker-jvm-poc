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
     *
     * @param file file to be checked support from this language plugin for.
     * @returns whether this language plugin supports the provided [file].
     */
    public fun supports(file: MutatableFile): Boolean

    /**
     * Generates the relevant mutants for the source file, and places the mutants in the target file.
     *
     * @param file file to mutate.
     * @param configuration configuration of the mutation process.
     * @returns the set of placed mutants.
     */
    public fun instrument(file: MutatableFile, configuration: MutationConfiguration): Set<Mutant>

}

