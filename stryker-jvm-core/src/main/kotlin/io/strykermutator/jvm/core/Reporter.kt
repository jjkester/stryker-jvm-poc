package io.strykermutator.jvm.core

import io.strykermutator.jvm.runner.MutationTestReport
import io.strykermutator.jvm.runner.TestExecutionResult

public interface Reporter {

    public fun collect(testExecutionResults: Collection<TestExecutionResult>): MutationTestReport
}

public class NullReporter : Reporter {
    override fun collect(testExecutionResults: Collection<TestExecutionResult>): MutationTestReport =
        object : MutationTestReport {}
}