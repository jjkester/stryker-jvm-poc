package io.strykermutator.jvm.language.placer

import io.strykermutator.jvm.common.CompanionMethodRef
import io.strykermutator.jvm.common.Mutant

/**
 * A mutant placer modifies the AST and replaces nodes in the original AST with new nodes that conditionally activate
 * a mutant or default to the original node.
 *
 * Mutant placers often implement a strategy applicable for a wide variety of nodes, with each implementation providing
 * a distinct way of conditionally activating a mutant because of specifics of the original node type. For example,
 * expressions may require different code to statements.
 *
 * Mutant placers implement mutant schemata, meaning that it implements the conditional activation of mutants. Stryker
 * JVM requires the Stryker companion mechanism to be used for this.
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
 * @param V type of original node.
 * @param R type of replacement node.
 */
public interface MutantPlacer<V, R> {

    /**
     * Places the [mutants] in the AST in place of the [node]. Generates conditional activation of mutants, defaulting
     * to the original node, calling the [referenced Stryker companion method][companionMethodRef].
     *
     * @param node the node to replace with mutants.
     * @param mutants the mutants to place.
     * @param companionMethodRef reference to the Stryker companion method to call for conditionally activating mutants.
     */
    public fun place(node: V, mutants: Map<Mutant, R>, companionMethodRef: CompanionMethodRef)
}