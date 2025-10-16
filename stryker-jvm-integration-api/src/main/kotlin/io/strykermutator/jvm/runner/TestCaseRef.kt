package io.strykermutator.jvm.runner

/**
 * Reference to an executable test case.
 *
 * This reference serves both as a reference to an already executed test, but also as a reference in a command to
 * execute a test. An implementation must therefore be able to uniquely identify a test based on the [name].
 */
public interface TestCaseRef {

    /**
     * Unique identifier of the test case.
     */
    public val name: String
}

/**
 * Default implementation of a reference to an executable test case.
 *
 * This reference serves both as a reference to an already executed test, but also as a reference in a command to
 * execute a test. An implementation must therefore be able to uniquely identify a test based on the [name].
 *
 * @property name the unique identifier of the test case.
 */
public data class DefaultTestCaseRef(val className: String, val testCaseName: String) : TestCaseRef {
    override val name: String = "$className#$testCaseName"
}
