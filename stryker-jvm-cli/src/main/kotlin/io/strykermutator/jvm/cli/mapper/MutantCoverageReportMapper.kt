package io.strykermutator.jvm.cli.mapper

import io.strykermutator.jvm.common.DefaultMutantRef
import io.strykermutator.jvm.common.dto.MutantCoverageReportDto
import io.strykermutator.jvm.common.dto.TestCoverageDto
import io.strykermutator.jvm.runner.DefaultMutantCoverageReport
import io.strykermutator.jvm.runner.DefaultTestCaseRef
import io.strykermutator.jvm.runner.MutantCoverageReport
import io.strykermutator.jvm.runner.TestCaseRef

public fun MutantCoverageReport.toDto(): MutantCoverageReportDto =
    MutantCoverageReportDto(testCases.associate {
        it.name to TestCoverageDto(getCoveredMutants(it).map { m -> m.id }.toSet())
    })

public fun MutantCoverageReportDto.toDomain(): MutantCoverageReport =
    DefaultMutantCoverageReport(
        coverage.map { (testCaseRef, mutantRefs) ->
            Pair<TestCaseRef, Set<DefaultMutantRef>>(
                parseTestCaseRef(testCaseRef),
                mutantRefs.mutantIds.map { mutantId ->
                    DefaultMutantRef(mutantId)
                }.toSet()
            )
        }.toMap()
    )

private fun parseTestCaseRef(name: String): DefaultTestCaseRef {
    val (className, testCaseName) = name.split("#", limit = 2)
    return DefaultTestCaseRef(className, testCaseName)
}