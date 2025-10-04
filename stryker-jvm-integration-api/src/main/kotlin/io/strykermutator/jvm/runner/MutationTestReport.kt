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
    public val mutants: Map<MutantRef, MutantState>

    /**
     * Mutation score between 0% and 100%.
     */
    public val mutationScore: Double
}

public data class DefaultMutationTestReport(override val mutants: Map<MutantRef, MutantState>) : MutationTestReport {

    override val mutationScore: Double by lazy {
        val (killed, survived) = mutants.entries.partition { it.value.isKilled }
        killed.size.toDouble() / survived.size.toDouble()
    }
}
