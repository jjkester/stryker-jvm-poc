package io.strykermutator.jvm

import io.strykermutator.jvm.common.StringCompanionMethodRef
import io.strykermutator.jvm.core.*
import io.strykermutator.jvm.runner.DefaultMutantCoverageReport
import io.strykermutator.jvm.runner.DefaultSourceRoot
import java.io.File

public class JavaMutationTestRunner {

    public fun run(sourcePath: String) {
        val companionMethodRef =
            StringCompanionMethodRef("io.strykermutator.jvm.companion.StrykerCompanion", "mutantActive")
        val testRunner = StrykerFactory {
            instrumenter = DefaultInstrumenter(LanguagePlugins.load())
            executionPlanner = NaiveExecutionPlanner()
            reporter = DefaultReporter()
        }
        val sourceRoot = DefaultSourceRoot(File(sourcePath))

        val targetDir = File("build/stryker-mutated-sources")
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }
        val target = DefaultSourceRoot(targetDir)

        val instrumentationResult = testRunner.instrumentSources(sourceRoot, target, companionMethodRef)

        val mutantCoverageReport = DefaultMutantCoverageReport(mapOf())

        val testPlan = testRunner.planExecution(mutantCoverageReport)

        println("Mutants planned for execution: ${testPlan.testExecutions.flatMap { it.activeMutants }}")

        val report = testRunner.compileReport(
            mutants = instrumentationResult.mutants,
            mutantCoverageReport = mutantCoverageReport,
            testExecutionResults = emptyList()
        )

        println("Report: $report")
    }

}