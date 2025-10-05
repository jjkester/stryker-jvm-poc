package io.strykermutator.jvm.language

import io.strykermutator.jvm.common.DefaultMutantRef
import io.strykermutator.jvm.common.MutantRef
import java.util.concurrent.atomic.AtomicInteger

/**
 * Generator for unique mutant identifiers.
 */
public interface MutantIdGenerator {

    /**
     * Creates a new mutant identifier and returns it as reference.
     */
    public fun create(): MutantRef

    public companion object {

        /**
         * Creates a new sequential mutant identifier generator using numbers, starting with 0.
         */
        @JvmStatic
        public fun sequential(): MutantIdGenerator = SequentialMutantIdGenerator()
    }
}

internal class SequentialMutantIdGenerator : MutantIdGenerator {

    private val nextId = AtomicInteger(0)

    override fun create(): MutantRef = DefaultMutantRef(nextId.getAndIncrement().toString())
}
