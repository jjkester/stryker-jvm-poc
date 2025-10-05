package io.strykermutator.jvm.language

import io.strykermutator.jvm.common.CompanionMethodRef

/**
 * Configuration of the mutation process.
 */
public interface MutationConfiguration {

    /**
     * Reference to the Stryker companion method to be called for each mutant.
     */
    public val companionMethodRef: CompanionMethodRef

    /**
     * Generator for unique mutant identifiers.
     */
    public val mutantIdGenerator: MutantIdGenerator
}

public data class DefaultMutationConfiguration(
    override val companionMethodRef: CompanionMethodRef,
    override val mutantIdGenerator: MutantIdGenerator
) : MutationConfiguration
