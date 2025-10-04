package io.strykermutator.jvm.core

import io.strykermutator.jvm.runner.Stryker

public class StrykerFactory {

    public var instrumenter: Instrumenter? = null
    public var executionPlanner: ExecutionPlanner? = null
    public var reporter: Reporter? = null

    public fun create(): Stryker = DefaultStryker(
        checkNotNull(instrumenter) { "Stryker instrumenter not provided" },
        checkNotNull(executionPlanner) { "Stryker execution planner not provided" },
        checkNotNull(reporter) { "Stryker reporter not provided" }
    )

    public companion object {

        public operator fun invoke(body: StrykerFactory.() -> Unit): Stryker = StrykerFactory().apply(body).create()
    }
}