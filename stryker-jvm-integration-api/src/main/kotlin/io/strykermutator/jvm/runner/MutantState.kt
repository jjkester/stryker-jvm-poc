package io.strykermutator.jvm.runner

/**
 * State a mutant can have after testing.
 */
public enum class MutantState {
    /** The mutant was caught by the test suite. */
    KILLED,

    /** The mutant survived. */
    SURVIVED,

    /** The mutant does not have any tests covering it. */
    NOT_COVERED,

    /** The mutant timed out. */
    TIMED_OUT,

    /** The test suite crashed with the mutant active. */
    CRASHED,

    /** The mutant did not compile. */
    INVALID;

    /** Whether the state reflects a killed mutant state. */
    public val isKilled: Boolean
        get() = this == KILLED || this == TIMED_OUT
}