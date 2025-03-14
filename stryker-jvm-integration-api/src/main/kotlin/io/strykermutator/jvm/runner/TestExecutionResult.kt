package io.strykermutator.jvm.runner

/**
 * Test results for set of test cases executed against a set of active mutants in a single test run.
 */
public interface TestExecutionResult {

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
 * @property testCases the executed test cases.
 * @property activeMutants the enabled mutants.
 * @constructor Creates a test execution result with a set of test cases, a set of active mutants, and a set of failed
 *              test cases.
 */
public data class DefaultTestExecutionResult(
    override val testCases: Set<TestCaseRef>,
    override val activeMutants: Set<MutantRef>,
    override val failedTestCases: Set<TestCaseRef>
) : TestExecutionResult {

    init {
        require(testCases.isNotEmpty()) { "Set of test cases is empty" }
        require(activeMutants.isNotEmpty()) { "Set of active mutants is empty" }
        require(testCases.containsAll(failedTestCases)) { "Set of test cases does not contain all failed test cases" }
    }

    /**
     * Creates a test execution result with a single test case and a single active mutant.
     *
     * @param testCase the executed test case.
     * @param activeMutant the enabled mutant.
     * @param isPassed whether the test case passed.
     */
    public constructor(testCase: TestCaseRef, activeMutant: MutantRef, isPassed: Boolean) : this(
        setOf(testCase),
        setOf(activeMutant),
        setOfNotNull(testCase.takeIf { !isPassed }),
    )
}
