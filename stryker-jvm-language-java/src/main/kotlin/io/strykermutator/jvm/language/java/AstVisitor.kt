package io.strykermutator.jvm.language.java

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.visitor.TreeVisitor
import io.strykermutator.jvm.common.*
import io.strykermutator.jvm.language.MutantIdGenerator
import io.strykermutator.jvm.language.java.operator.JavaMutationOperator
import kotlin.jvm.optionals.getOrNull
import com.github.javaparser.Position as JavaParserPosition

internal class AstVisitor(
    private val file: MutatableFile,
    private val operators: Collection<JavaMutationOperator>,
    private val mutantIdGenerator: MutantIdGenerator,
    private val collector: (node: Node, mutant: Mutant, replacement: Node) -> Unit
) : TreeVisitor() {

    override fun process(node: Node) {
        operators.forEach { operator ->
            operator.mutate(node).forEach { replacement ->
                collector(
                    node,
                    DefaultMutant(
                        mutantIdGenerator.create(),
                        file,
                        node.positionIn(file),
                        operator.name,
                        replacement.toString()
                    ),
                    replacement
                )
            }
        }
    }

    private fun Node.positionIn(file: MutatableFile): LocationSegment =
        LocationSegment(
            file.original,
            checkNotNull(begin.getOrNull()).convert()..checkNotNull(end.getOrNull()).convert()
        )

    private fun JavaParserPosition.convert(): Position = Position(line, column)
}