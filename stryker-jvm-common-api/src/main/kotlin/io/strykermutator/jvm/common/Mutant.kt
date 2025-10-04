package io.strykermutator.jvm.common

/**
 * Mutant introduced in code for mutation testing.
 */
public interface Mutant {

    /** Unique reference to the mutant. */
    public val ref: MutantRef

    /** Location of the original code in the source file that is replaced by this mutant. */
    public val location: LocationSegment

    /** Name of the mutation operator that introduced the mutant. */
    public val operator: String

    /** String representation of the mutant code that replaced the original code. */
    public val replacement: String
}

/**
 * Default implementation of a mutant.
 *
 * @property ref unique reference to the mutant.
 * @property location location of the original code in the source file that is replaced by this mutant.
 * @property operator name of the mutation operator that introduced the mutant.
 * @property replacement string representation of the mutant code that replaced the original code.
 * @constructor Creates a new mutant with the provided properties.
 */
public data class DefaultMutant(
    override val ref: MutantRef,
    override val location: LocationSegment,
    override val operator: String,
    override val replacement: String
) : Mutant