package io.strykermutator.jvm.core

import io.strykermutator.jvm.common.Mutant
import io.strykermutator.jvm.common.MutantRef
import io.strykermutator.jvm.runner.*

public interface Reporter {

    public fun collect(
        mutants: Collection<Mutant>,
        mutantCoverageReport: MutantCoverageReport,
        testExecutionResults: Collection<TestExecutionResult>
    ): MutationTestReport
}

public class DefaultReporter : Reporter {
    override fun collect(
        mutants: Collection<Mutant>,
        mutantCoverageReport: MutantCoverageReport,
        testExecutionResults: Collection<TestExecutionResult>
    ): MutationTestReport {
        val coveredMutantResults = testExecutionResults.asSequence()
            .flatMap { it.splitMutants(mutantCoverageReport) }
            .groupingBy { (mutant, _) -> mutant }
            .aggregate<_, _, DefaultMutationTestReport.DefaultMutantResult> { _, accumulator, (_, testOutcomes), _ ->
                val mergedTestOutcomes = if (accumulator == null) {
                    testOutcomes
                } else {
                    (accumulator.testOutcomes.keys + testOutcomes.keys)
                        .associateWith {
                            (accumulator.testOutcomes.values.asSequence() + testOutcomes.values.asSequence())
                                .maxWith(testOutcomeComparator)
                        }
                }

                DefaultMutationTestReport.DefaultMutantResult(
                    mergedTestOutcomes.values.toSet().toMutantState(),
                    mergedTestOutcomes
                )
            }

        val completeMutantResults = mutants
            .asSequence()
            .distinct()
            .map(Mutant::ref)
            .associateWith { mutantRef ->
                coveredMutantResults[mutantRef] ?: DefaultMutationTestReport.DefaultMutantResult.notCovered()
            }

        return DefaultMutationTestReport(completeMutantResults)
    }

    private fun TestExecutionResult.splitMutants(
        mutantCoverageReport: MutantCoverageReport
    ): Sequence<Pair<MutantRef, Map<TestCaseRef, TestOutcome>>> = activeMutants.asSequence().map { mutant ->
        val coveringTestCases = mutantCoverageReport.getCoveringTestCases(mutant)
        mutant to testOutcomes.filterKeys { it in coveringTestCases }
    }

    private fun Set<TestOutcome>.toMutantState(): MutantState = when {
        isEmpty() -> MutantState.NOT_COVERED
        contains(TestOutcome.FAILED) -> MutantState.KILLED
        contains(TestOutcome.CRASHED) -> MutantState.CRASHED
        contains(TestOutcome.TIMED_OUT) -> MutantState.TIMED_OUT
        else -> MutantState.SURVIVED
    }

    private companion object {

        private val testOutcomeComparator: Comparator<TestOutcome> = Comparator.comparingInt {
            // Higher value trumps lower value when multiple outcomes for the same test are found
            when (it) {
                // Failing test means killed, making the others irrelevant
                TestOutcome.FAILED -> 3
                // Runtime crash means killed,
                TestOutcome.CRASHED -> 2
                // Timeout is a weak indicator of a killed mutant
                TestOutcome.TIMED_OUT -> 1
                // Passing test implies nothing and is only relevant if there are no other outcomes
                TestOutcome.PASSED -> 0
            }
        }

    }
}