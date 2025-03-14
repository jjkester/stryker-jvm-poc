package io.strykermutator.jvm.runner

/**
 * Plan of test case executions for collecting mutation test results.
 */
public interface MutationTestPlan {

    /**
     * Test executions to run.
     */
    public val testExecutions: Set<TestExecution>
}

/**
 * Default implementation of a plan of test case executions for collecting mutation test results.
 *
 * @property testExecutions the test executions to run.
 * @constructor Creates a mutation test plan with a set of test executions.
 */
public data class DefaultMutationTestPlan(override val testExecutions: Set<TestExecution>) : MutationTestPlan
