package io.strykermutator.jvm.cli.mapper

import io.strykermutator.jvm.common.dto.MutationTestPlanDto
import io.strykermutator.jvm.common.dto.TestExecutionDto
import io.strykermutator.jvm.runner.MutationTestPlan

public fun MutationTestPlan.toDto(): MutationTestPlanDto = MutationTestPlanDto(
    testExecutions = testExecutions.map { testExecution ->
        TestExecutionDto(
            testExecution.testCases.map { it.name }.toSet(),
            testExecution.activeMutants.map { it.id }.toSet())
    }.toSet()
)