package io.strykermutator.jvm.runner

/**
 * Outcome of a test in regard to mutation testing.
 */
public enum class TestOutcome {
    /**
     * The test passed. When a mutant is active, this indicates the mutant survived.
     */
    PASSED,

    /**
     * The test failed regularly. When a mutant is active, this indicates that the mutant is killed.
     */
    FAILED,

    /**
     * The test failed due to a runtime exception. When a mutant is active, this indicates that the mutant is killed.
     */
    CRASHED,

    /**
     * The test is considered to have failed due to a timeout introduced by a mutant. A timeout must only occur when
     * a mutant is active, and indicates that the mutant is killed.
     */
    TIMED_OUT;

    /**
     * Whether test passed.
     */
    public val isSuccessful: Boolean get() = this == PASSED
}