package io.strykermutator.jvm.runner

/**
 * TODO: Define mutation test report interface
 */
public interface MutationTestReport {
    public val killed: Int
    public val survived: Int
}

public data class DefaultMutationTestReport(override val killed: Int, override val survived: Int) : MutationTestReport
