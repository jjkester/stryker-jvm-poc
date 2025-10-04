package io.strykermutator.jvm.language.operator

import kotlinx.coroutines.flow.Flow

/**
 * Asynchronously executing mutation operator using Coroutines.
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
 * A Coroutines implementation is convenient as found mutants can be emitted when found, and no default value has to be
 * returned when a node is not supported. Additionally, these implementations allows for parallelization.
 *
 * @param V type of original node.
 * @param R type of replacement node.
 */
public interface CoroutinesMutationOperator<in V : Any, out R : Any> : MutationOperator<V, R> {

    /**
     * Finds mutants for the [node] if this operator can mutate this node, and emits the replacement nodes for
     * mutants. The returned flow completes without any values when the node cannot be mutated by this operator.
     *
     * It is recommended to implement this method using the flow builder.
     *
     * @param node AST node to mutate.
     * @return flow of replacement nodes for mutants.
     */
    public fun mutate(node: V): Flow<R>
}