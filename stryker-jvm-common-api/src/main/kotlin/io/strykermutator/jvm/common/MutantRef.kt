package io.strykermutator.jvm.common

/**
 * Reference to a mutant.
 */
public interface MutantRef {

    /**
     * Identifier of the mutant.
     */
    public val id: String
}

/**
 * Default implementation of a reference to a mutant.
 *
 * @property id the identifier of the mutant.
 * @constructor Creates a reference to the mutant by its identifier.
 */
public data class DefaultMutantRef(override val id: String) : MutantRef
