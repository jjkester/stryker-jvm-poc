package io.strykermutator.jvm.core

import io.strykermutator.jvm.runner.DefaultMutationTestReport
import io.strykermutator.jvm.runner.MutationTestReport
import io.strykermutator.jvm.runner.TestExecutionResult

public interface Reporter {

    public fun collect(testExecutionResults: Collection<TestExecutionResult>): MutationTestReport
}

public class DefaultReporter : Reporter {
    override fun collect(testExecutionResults: Collection<TestExecutionResult>): MutationTestReport {
        // TODO: Implement actual numbers
        return DefaultMutationTestReport(emptyMap())
    }
}