package io.strykermutator.jvm.core

import io.strykermutator.jvm.common.CompanionMethodRef
import io.strykermutator.jvm.common.Mutant
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
    ): InstrumentationResult {
        val configuration = DefaultMutationConfiguration(companionMethodRef, MutantIdGenerator.sequential())
        return instrumenter.instrument(sourceRoot, target, configuration)
    }

    override fun planExecution(mutantCoverageReport: MutantCoverageReport): MutationTestPlan =
        executionPlanner.plan(mutantCoverageReport)

    override fun compileReport(
        mutants: Collection<Mutant>,
        mutantCoverageReport: MutantCoverageReport,
        testExecutionResults: Collection<TestExecutionResult>
    ): MutationTestReport =
        reporter.collect(mutants, mutantCoverageReport, testExecutionResults)
}