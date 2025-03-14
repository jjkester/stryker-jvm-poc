package io.strykermutator.jvm.runner

/**
 * Reference to a mutant.
 */
public interface MutantRef {

    /**
     * Identifier of the mutant.
     */
    public val id: Int
}

/**
 * Default implementation of a reference to a mutant.
 *
 * @property id the identifier of the mutant.
 * @constructor Creates a reference to the mutant by its identifier.
 */
public data class DefaultMutantRef(override val id: Int) : MutantRef
