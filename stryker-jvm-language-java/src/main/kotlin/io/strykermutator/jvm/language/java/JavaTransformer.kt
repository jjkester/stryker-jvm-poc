package io.strykermutator.jvm.language.java

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.Expression
import com.github.javaparser.ast.visitor.ObjectIdentityEqualsVisitor
import com.github.javaparser.ast.visitor.ObjectIdentityHashCodeVisitor
import com.github.javaparser.utils.VisitorMap
import io.strykermutator.jvm.common.Mutant
import io.strykermutator.jvm.common.MutatableFile
import io.strykermutator.jvm.language.MutationConfiguration
import io.strykermutator.jvm.language.Transformer
import io.strykermutator.jvm.language.java.operator.JavaMutationOperator
import io.strykermutator.jvm.language.java.placer.ExpressionMutantPlacer

public class JavaTransformer(
    private val operators: Collection<JavaMutationOperator>,
    private val expressionMutantPlacer: ExpressionMutantPlacer
) : Transformer {

    override fun transform(
        file: MutatableFile,
        configuration: MutationConfiguration
    ): Set<Mutant> {
        val mutants = VisitorMap<Node, MutableMap<Mutant, Node>>(ObjectIdentityHashCodeVisitor(), ObjectIdentityEqualsVisitor())
        val visitor = AstVisitor(file, operators, configuration.mutantIdGenerator) { node, mutant, replacement ->
            mutants.getOrPut(node) { mutableMapOf() }[mutant] = replacement
        }

        val compilationUnit = StaticJavaParser.parse(file.original)
        visitor.visitPreOrder(compilationUnit)

        mutants.forEach { (node, replacements) ->
            when (node) {
                is Expression -> expressionMutantPlacer.place(
                    node,
                    replacements.checkNodeTypes(),
                    configuration.companionMethodRef
                )
            }
        }
        if (!file.mutated.parentFile.exists()) {
            file.mutated.parentFile.mkdirs()
        }
        file.mutated.writeText(compilationUnit.toString())

        return mutants.values.flatMapTo(mutableSetOf()) { it.keys }
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T : Node> Map<Mutant, Node>.checkNodeTypes(): Map<Mutant, T> = apply {
        check(values.all { it is T })
    } as Map<Mutant, T>


}
