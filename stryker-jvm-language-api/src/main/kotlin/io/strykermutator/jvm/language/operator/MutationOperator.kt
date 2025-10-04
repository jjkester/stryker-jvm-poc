package io.strykermutator.jvm.language.operator

/**
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
 * This interface cannot be implemented directly, instead the [SynchronousMutationOperator] or
 * [CoroutinesMutationOperator] should be implemented.
 *
 * @param V type of original node.
 * @param R type of replacement node.
 */
public sealed interface MutationOperator<in V : Any, out R : Any> {

    /** Unique name of the operator. The name must follow the Stryker naming conventions. */
    public val name: String
}

