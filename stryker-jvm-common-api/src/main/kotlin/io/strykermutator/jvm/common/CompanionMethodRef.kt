package io.strykermutator.jvm.common

/**
 * Reference to the Stryker companion method used for mutation switching and mutation coverage.
 */
public interface CompanionMethodRef {

    /** Qualified name of the class containing the method. */
    public val qualifiedClassName: String

    /** Name of the static method on the class. */
    public val methodName: String
}

/**
 * Stryker companion method reference implementation using raw strings.
 *
 * @property qualifiedClassName qualified name of the class containing the method.
 * @property methodName name of the static method on the class.
 * @constructor Creates a reference to a Stryker companion method by providing the class and method name.
 */
public data class StringCompanionMethodRef(
    override val qualifiedClassName: String,
    override val methodName: String
): CompanionMethodRef
