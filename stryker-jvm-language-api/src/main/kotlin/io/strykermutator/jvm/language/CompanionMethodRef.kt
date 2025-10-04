package io.strykermutator.jvm.language

public interface CompanionMethodRef {
    public val qualifiedClassName: String
    public val methodName: String
}

public data class StringCompanionMethodRef(
    override val qualifiedClassName: String,
    override val methodName: String
): CompanionMethodRef
