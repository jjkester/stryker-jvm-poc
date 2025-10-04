package io.strykermutator.jvm.language.java.placer

import io.strykermutator.jvm.language.Mutant

public interface MutantPlacer<A, R> {

    public fun place(node: A, mutants: Map<Mutant, R>)
}