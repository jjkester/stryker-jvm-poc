package io.strykermutator.jvm.core

import io.strykermutator.jvm.runner.MutantCoverageReport
import io.strykermutator.jvm.runner.MutationTestPlan
import io.strykermutator.jvm.runner.MutationTestReport
import io.strykermutator.jvm.runner.MutationTestRunner
import io.strykermutator.jvm.runner.SourceRoot
import io.strykermutator.jvm.runner.TestExecutionResult

public class DefaultMutationTestRunner(
    private val instrumenter: Instrumenter,
    private val executionPlanner: ExecutionPlanner,
    private val reporter: Reporter
) : MutationTestRunner {
    override fun instrumentSources(
        sourceRoot: SourceRoot,
        target: SourceRoot
    ) {
        instrumenter.instrument(sourceRoot, target)
    }

    override fun planExecution(mutantCoverageReport: MutantCoverageReport): MutationTestPlan =
        executionPlanner.plan(mutantCoverageReport)

    override fun compileReport(testExecutionResults: Collection<TestExecutionResult>): MutationTestReport =
        reporter.collect(testExecutionResults)
}