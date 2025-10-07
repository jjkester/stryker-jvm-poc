package io.strykermutator.jvm.runner

import io.strykermutator.jvm.common.MutantRef

/**
 * Minimal representation of a mutation test report.
 *
 * TODO: Provide sufficient data for the mutation-testing-elements report.
 */
public interface MutationTestReport {

    /**
     * State of each mutant after mutation testing.
     */
    public val mutants: Map<MutantRef, MutantResult>

    /**
     * Mutation score between 0% and 100%.
     */
    public val mutationScore: Double

    /**
     * Mutation test result of a single mutant.
     */
    public interface MutantResult {

        /**
         * State of the mutant after testing.
         */
        public val state: MutantState

        /**
         * Test cases that covered this mutant and their outcomes.
         */
        public val testOutcomes: Map<TestCaseRef, TestOutcome>
    }
}

public data class DefaultMutationTestReport(override val mutants: Map<MutantRef, DefaultMutantResult>) :
    MutationTestReport {

    override val mutationScore: Double by lazy {
        val (killed, survived) = mutants.entries.partition { it.value.state.isKilled }
        killed.size.toDouble() / survived.size.toDouble()
    }

    public data class DefaultMutantResult(
        override val state: MutantState,
        override val testOutcomes: Map<TestCaseRef, TestOutcome>
    ) : MutationTestReport.MutantResult {

        public companion object {

            private val NOT_COVERED = DefaultMutantResult(MutantState.NOT_COVERED, emptyMap())

            public fun notCovered(): DefaultMutantResult = NOT_COVERED
        }
    }
}
