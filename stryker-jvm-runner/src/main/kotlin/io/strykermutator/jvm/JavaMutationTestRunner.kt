package io.strykermutator.jvm

import io.strykermutator.jvm.core.DefaultInstrumenter
import io.strykermutator.jvm.core.DefaultMutationTestRunner
import io.strykermutator.jvm.core.NaiveExecutionPlanner
import io.strykermutator.jvm.core.NullReporter
import io.strykermutator.jvm.language.java.JavaMutantGenerator
import io.strykermutator.jvm.runner.DefaultMutantCoverageReport
import io.strykermutator.jvm.runner.DefaultSourceRoot
import java.io.File

public class JavaMutationTestRunner {

    public fun run() {
        val generators = setOf(JavaMutantGenerator())
        val testRunner = DefaultMutationTestRunner(
            instrumenter = DefaultInstrumenter(generators),
            executionPlanner = NaiveExecutionPlanner(),
            reporter = NullReporter()
        )
        val sourceRoot = DefaultSourceRoot(File("C:\\Users\\JelleH\\IdeaProjects\\stryker-jvm-poc\\stryker-jvm-runner\\testProjects\\helloWorld\\src\\main\\java"))

        val targetDir = File("build/stryker-mutated-sources")
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }
        val target = DefaultSourceRoot(targetDir)

        testRunner.instrumentSources(sourceRoot, target)

        val testPlan = testRunner.planExecution(DefaultMutantCoverageReport(mapOf()))

        println("Mutants planned for execution: ${testPlan.testExecutions.flatMap { it.activeMutants }}")

        val report = testRunner.compileReport(emptyList())

        println("Report: $report")
    }

}

public fun main(args: Array<String>) {
    JavaMutationTestRunner().run()
}