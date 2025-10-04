package io.strykermutator.jvm.core

import io.strykermutator.jvm.common.MutantRef
import io.strykermutator.jvm.runner.MutantCoverageReport
import io.strykermutator.jvm.runner.MutationTestPlan
import io.strykermutator.jvm.runner.TestCaseRef
import io.strykermutator.jvm.runner.TestExecution

public interface ExecutionPlanner {

    public fun plan(mutantCoverageReport: MutantCoverageReport): MutationTestPlan
}

public class NaiveExecutionPlanner : ExecutionPlanner {

    override fun plan(mutantCoverageReport: MutantCoverageReport): MutationTestPlan = object : MutationTestPlan {
        override val testExecutions: Set<TestExecution> = mutantCoverageReport.coveredMutants.mapTo(
            destination = mutableSetOf(),
            transform = {
                object : TestExecution {
                    override val testCases: Set<TestCaseRef> = mutantCoverageReport.testCases
                    override val activeMutants: Set<MutantRef> = setOf(it)
                }
            })
    }
}
