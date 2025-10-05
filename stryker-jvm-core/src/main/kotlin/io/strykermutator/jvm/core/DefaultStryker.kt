package io.strykermutator.jvm.core

import io.strykermutator.jvm.common.CompanionMethodRef
import io.strykermutator.jvm.language.DefaultMutationConfiguration
import io.strykermutator.jvm.language.MutantIdGenerator
import io.strykermutator.jvm.runner.*

internal class DefaultStryker(
    private val instrumenter: Instrumenter,
    private val executionPlanner: ExecutionPlanner,
    private val reporter: Reporter
) : Stryker {
    override fun instrumentSources(
        sourceRoot: SourceRoot,
        target: SourceRoot,
        companionMethodRef: CompanionMethodRef
    ) {
        val configuration = DefaultMutationConfiguration(companionMethodRef, MutantIdGenerator.sequential())
        instrumenter.instrument(sourceRoot, target, configuration)
    }

    override fun planExecution(mutantCoverageReport: MutantCoverageReport): MutationTestPlan =
        executionPlanner.plan(mutantCoverageReport)

    override fun compileReport(testExecutionResults: Collection<TestExecutionResult>): MutationTestReport =
        reporter.collect(testExecutionResults)
}