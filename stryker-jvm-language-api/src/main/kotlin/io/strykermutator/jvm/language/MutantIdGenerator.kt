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
}

/**
 * Mutant identifier generator using sequentially incrementing numbers, starting with 0.
 *
 * This implementation is thread-safe.
 */
public class SequentialMutantIdGenerator : MutantIdGenerator {

    private val nextId = AtomicInteger(0)

    /**
     * Creates a new mutant identifier and returns it as reference.
     */
    override fun create(): MutantRef = DefaultMutantRef(nextId.getAndIncrement().toString())
}
