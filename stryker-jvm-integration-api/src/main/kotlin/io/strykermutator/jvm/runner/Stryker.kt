package io.strykermutator.jvm.runner

import io.strykermutator.jvm.common.CompanionMethodRef
import io.strykermutator.jvm.common.Mutant

/**
 * Stryker entry points for mutation testing runners.
 *
 * The mutation test runner is responsible for inserting mutations in the sources, planning the execution of tests,
 * and processing the results.
 *
 * A mutation test run always consists of the following steps, some of which are specified by this interface:
 *
 * - Mutate the source code, implemented by [instrumentSources];
 * - Analyze mutation coverage;
 * - Plan the test execution, implemented by [planExecution];
 * - Test the mutated source code;
 * - Compile a report, implemented by [compileReport].
 *
 * The steps not implemented by this interface must be orchestrated by the caller. The exact actions for these steps
 * differs between tools, libraries and languages, therefore a specific implementation is necessary.
 *
 * Mutation test runners can be stateless. By delegating orchestration of the mutation test process and keeping state to
 * a build tool, the integration with the build tool can be optimized. For example, a build tool can use its built-in
 * caching mechanisms and detect changes to optimize performance.
 */
public interface Stryker {

    /**
     * Instruments the source files in the source root by inserting mutants and writing the mutated code to the target
     * directory, preserving the directory structure and file names.
     *
     * @param sourceRoot the root directory of the source files to read.
     * @param target the target directory for the instrumented sources.
     * @param companionMethodRef the Stryker companion method to call for each mutant.
     * @return the results of instrumenting the sources.
     */
    public fun instrumentSources(sourceRoot: SourceRoot, target: SourceRoot, companionMethodRef: CompanionMethodRef): InstrumentationResult

    /**
     * Plans the execution of test cases with active mutants. The resulting mutation test plan [MutationTestPlan] can
     * be executed to efficiently collect the necessary results to create a mutation test report.
     *
     * The mutation test plan is based on the mutant coverage report [MutantCoverageReport] obtained from running
     * coverage analysis on the mutated sources.
     *
     * @param mutantCoverageReport the mutant coverage report containing the coverage of mutants by test cases.
     * @return the mutation test plan to execute.
     */
    public fun planExecution(mutantCoverageReport: MutantCoverageReport): MutationTestPlan

    /**
     * Compiles the results from executing a mutation test plan [MutationTestPlan] into a mutation test report.
     *
     * @param mutants the mutants that were instrumented.
     * @param mutantCoverageReport the test coverage of each mutant.
     * @param testExecutionResults the results of executing the mutation test plan.
     * @return the mutation test report.
     */
    public fun compileReport(
        mutants: Collection<Mutant>,
        mutantCoverageReport: MutantCoverageReport,
        testExecutionResults: Collection<TestExecutionResult>
    ): MutationTestReport
}
