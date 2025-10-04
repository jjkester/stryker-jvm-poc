package io.strykermutator.jvm.runner

import io.strykermutator.jvm.common.MutantRef

/**
 * Test results for set of test cases executed against a set of active mutants in a single test run.
 */
public interface TestExecutionResult {

    /**
     * Outcome of each test case.
     */
    public val testOutcomes: Map<TestCaseRef, TestOutcome>

    /**
     * Executed test cases.
     */
    public val testCases: Set<TestCaseRef>

    /**
     * Enabled mutants.
     */
    public val activeMutants: Set<MutantRef>

    /**
     * Failed test cases. Contains all test cases that did not pass. Must be a subset of [testCases].
     */
    public val failedTestCases: Set<TestCaseRef>
}

/**
 * Default implementation of a test execution containing a set of test cases and a set of active mutants that can be
 * combined in a single test run.
 *
 * Validates that both the set of test cases and the set of active mutants are not empty, and that the set of failed
 * test cases is a subset of the set of test cases.
 *
 * @property testOutcomes the executed test cases and their outcomes.
 * @property activeMutants the enabled mutants.
 * @constructor Creates a test execution result with a set of test cases, a set of active mutants, and a set of failed
 *              test cases.
 */
public data class DefaultTestExecutionResult(
    override val testOutcomes: Map<TestCaseRef, TestOutcome>,
    override val activeMutants: Set<MutantRef>
) : TestExecutionResult {

    override val testCases: Set<TestCaseRef>
        get() = testOutcomes.keys

    override val failedTestCases: Set<TestCaseRef> by lazy {
        testOutcomes.mapNotNullTo(mutableSetOf()) { (testCaseRef, outcome) ->
            testCaseRef.takeIf { outcome.isSuccessful }
        }
    }

    init {
        require(testOutcomes.isNotEmpty()) { "Map of test cases and outcomes is empty" }
        require(activeMutants.isNotEmpty()) { "Set of active mutants is empty" }
    }
}
