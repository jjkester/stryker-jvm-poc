package io.strykermutator.jvm.common.dto

import kotlinx.serialization.Serializable

@Serializable
public data class MutationTestPlanDto(
    public val testExecutions: Set<TestExecutionDto>
){}

@Serializable
public data class TestExecutionDto(
    public val testCases: Set<String>,
    public val activeMutants: Set<String>
) {}