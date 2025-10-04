package io.strykermutator.jvm.language.java.operator

import com.github.javaparser.ast.Node

public interface MutationOperator<V, R> {

    public val name: String

    public fun mutate(node: V): Set<R>
}

public interface JavaMutationOperator : MutationOperator<Node, Node>
