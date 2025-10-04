package io.strykermutator.jvm.runner

import io.strykermutator.jvm.common.MutantRef

/**
 * Test execution containing a set of test cases and a set of active mutants that can be combined in a single test run.
 */
public interface TestExecution {

    /**
     * Test cases to execute.
     */
    public val testCases: Set<TestCaseRef>

    /**
     * Mutants to enable.
     */
    public val activeMutants: Set<MutantRef>
}

/**
 * Default implementation of a test execution containing a set of test cases and a set of active mutants that can be
 * combined in a single test run.
 *
 * Validates that both the set of test cases and the set of active mutants are not empty.
 *
 * @property testCases the test cases to execute.
 * @property activeMutants the mutants to enable.
 * @constructor Creates a test execution with a set of test cases and a set of active mutants.
 */
public data class DefaultTestExecution(
    override val testCases: Set<TestCaseRef>,
    override val activeMutants: Set<MutantRef>
) : TestExecution {

    init {
        require(testCases.isNotEmpty()) { "Set of test cases is empty" }
        require(activeMutants.isNotEmpty()) { "Set of active mutants is empty" }
    }

    /**
     * Creates a test execution with a single test case and a single active mutant.
     *
     * @param testCase the test case to execute.
     * @param activeMutant the mutant to enable.
     */
    public constructor(testCase: TestCaseRef, activeMutant: MutantRef) : this(setOf(testCase), setOf(activeMutant))
}
