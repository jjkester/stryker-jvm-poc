package io.strykermutator.jvm.language.java

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.Expression
import io.strykermutator.jvm.language.Mutant
import io.strykermutator.jvm.language.MutantIdGenerator
import io.strykermutator.jvm.language.java.operator.JavaMutationOperator
import io.strykermutator.jvm.language.java.placer.ExpressionMutantPlacer
import java.io.File

public class JavaTransformer(
    private val operators: Collection<JavaMutationOperator>,
    private val expressionMutantPlacer: ExpressionMutantPlacer
) : Transformer {

    override fun transform(
        sourceFile: File,
        targetFile: File,
        mutantIdGenerator: MutantIdGenerator
    ): Set<Mutant> {
        val mutants = mutableMapOf<Node, MutableMap<Mutant, Node>>()
        val visitor = AstVisitor(operators, mutantIdGenerator) { node, mutant, replacement ->
            mutants.getOrPut(node) { mutableMapOf() }[mutant] = replacement
        }

        val compilationUnit = StaticJavaParser.parse(sourceFile)
        visitor.visitPreOrder(compilationUnit)

        mutants.forEach { (node, replacements) ->
            when (node) {
                is Expression -> expressionMutantPlacer.place(node, replacements.checkTypes())
            }
        }

        targetFile.writeText(compilationUnit.toString())

        return mutants.values.flatMapTo(mutableSetOf()) { it.keys }
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T> Map<Mutant, Node>.checkTypes(): Map<Mutant, T> = apply {
        check(values.all { it is T })
    } as Map<Mutant, T>
}