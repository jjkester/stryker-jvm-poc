package io.strykermutator.jvm.language

import java.util.concurrent.atomic.AtomicInteger

public interface MutantIdGenerator {

    public fun new(): String
}

public class SequentialMutantIdGenerator : MutantIdGenerator {

    private val nextId = AtomicInteger(0)

    override fun new(): String = nextId.getAndIncrement().toString()
}
