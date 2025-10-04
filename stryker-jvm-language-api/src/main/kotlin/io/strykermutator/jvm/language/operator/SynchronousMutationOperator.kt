package io.strykermutator.jvm.language.operator

/**
 * Synchronously executing mutation operator.
 *
 * A mutation operator provides replacement AST nodes ("mutants") for a given AST node of the original source code.
 *
 * This interface supports a different generic type parameter for the original node and the replacement to allow
 * implementations more freedom in typing, and allow easy use of AST implementations that wrap nodes. It is expected
 * that most implementations will provide the same type for both parameters.
 *
 * It is recommended for language modules to create a language-specific mutation operator interface that constrains the
 * generic type parameters for that implementation.
 *
 * Mutation operators may have constructor arguments for configuration. In case no configuration is possible, it is
 * recommended to make implementations singleton, for example by using a Kotlin `object`.
 *
 * A synchronous implementation is safe but may not offer the most convenience or performance.
 *
 * @param V type of original node.
 * @param R type of replacement node.
 */
public interface SynchronousMutationOperator<in V : Any, out R : Any> : MutationOperator<V, R> {

    /**
     * Finds mutants for the [node] if this operator can mutate this node, and returns the replacement nodes for
     * mutants. Must return an empty set when the node cannot be mutated by this operator.
     *
     * @param node AST node to mutate.
     * @return set of replacement nodes for mutants.
     */
    public fun mutate(node: V): Set<R>
}