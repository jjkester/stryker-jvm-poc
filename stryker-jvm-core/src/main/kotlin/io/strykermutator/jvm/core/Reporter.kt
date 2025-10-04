package io.strykermutator.jvm.core

import io.strykermutator.jvm.runner.DefaultMutationTestReport
import io.strykermutator.jvm.runner.MutationTestReport
import io.strykermutator.jvm.runner.TestExecutionResult

public interface Reporter {

    public fun collect(testExecutionResults: Collection<TestExecutionResult>): MutationTestReport
}

public class DefaultReporter : Reporter {
    override fun collect(testExecutionResults: Collection<TestExecutionResult>): MutationTestReport {
        // TODO: Fix assumption that a failed test case fails all the mutants
        val (survived, killed) = testExecutionResults.partition { it.failedTestCases.isNotEmpty() }
        return DefaultMutationTestReport(killed.size, survived.size)
    }
}