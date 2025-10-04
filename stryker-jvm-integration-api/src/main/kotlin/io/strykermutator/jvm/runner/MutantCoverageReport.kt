package io.strykermutator.jvm.runner

import io.strykermutator.jvm.common.MutantRef

/**
 * Test coverage report of generated mutants in source code.
 *
 * The data for a mutant coverage report can be gathered by running test cases with coverage on mutated code without any
 * mutants enabled. A mutant coverage report is only valid if all test cases pass.
 *
 * Only test cases referenced in this report can be used during the mutation testing process. Excluded test cases will
 * not be able to detect mutants.
 */
public interface MutantCoverageReport {

    /**
     * Set of all executed test cases.
     */
    public val testCases: Set<TestCaseRef>

    /**
     * Set of all covered mutants.
     */
    public val coveredMutants: Set<MutantRef>

    /**
     * Returns the set of test cases covering the mutant.
     *
     * @param mutant the mutant covered by the test cases.
     * @return the test cases covering the mutant.
     */
    public fun getCoveringTestCases(mutant: MutantRef): Set<TestCaseRef>

    /**
     * Returns the set of mutants covered by the test case.
     *
     * @param testCase the test case covering the mutants.
     * @return the mutants covering the test case.
     */
    public fun getCoveredMutants(testCase: TestCaseRef): Set<MutantRef>
}

/**
 * Default implementation of a test coverage report of generated mutants in source code.
 *
 * This implementation uses the [coveredMutantsByTestCase] map as data source. All referenced test cases must pass
 * without any mutants enabled.
 *
 * The data for a mutant coverage report can be gathered by running test cases with coverage on mutated code without any
 * mutants enabled. A mutant coverage report is only valid if all test cases pass.
 *
 * Only test cases referenced in this report can be used during the mutation testing process. Excluded test cases will
 * not be able to detect mutants.
 *
 * @property coveredMutantsByTestCase the map containing the covered mutants for each test case.
 * @constructor Creates a mutant coverage report from a map containing the set of mutants covered by each test case.
 */
public data class DefaultMutantCoverageReport(
    private val coveredMutantsByTestCase: Map<TestCaseRef, Set<MutantRef>>
) : MutantCoverageReport {

    /**
     * Set of all executed test cases.
     */
    override val testCases: Set<TestCaseRef>
        get() = coveredMutantsByTestCase.keys

    /**
     * Set of all covered mutants.
     */
    override val coveredMutants: Set<MutantRef>
        get() = coveredMutantsByTestCase.flatMapTo(mutableSetOf()) { it.value }

    /**
     * Returns the set of test cases covering the mutant.
     *
     * @param mutant the mutant covered by the test cases.
     * @return the test cases covering the mutant.
     */
    override fun getCoveringTestCases(mutant: MutantRef): Set<TestCaseRef> {
        return coveredMutantsByTestCase.mapNotNullTo(mutableSetOf()) { (testCase, mutants) ->
            testCase.takeIf { mutant in mutants }
        }
    }

    /**
     * Returns the set of mutants covered by the test case.
     *
     * @param testCase the test case covering the mutants.
     * @return the mutants covering the test case.
     */
    override fun getCoveredMutants(testCase: TestCaseRef): Set<MutantRef> {
        return coveredMutantsByTestCase[testCase] ?: emptySet()
    }
}
