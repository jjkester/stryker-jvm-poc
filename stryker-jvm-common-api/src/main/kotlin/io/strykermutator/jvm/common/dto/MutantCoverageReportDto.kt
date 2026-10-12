package io.strykermutator.jvm.common.dto

import kotlinx.serialization.Serializable

@Serializable
public data class MutantCoverageReportDto(
    public val coverage: Map<String, TestCoverageDto>
)

@Serializable
public data class TestCoverageDto(
    public val mutantIds: Set<String>
)