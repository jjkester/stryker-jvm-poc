package io.strykermutator.jvm.language.java

import com.github.javaparser.Position
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.visitor.TreeVisitor
import io.strykermutator.jvm.language.LocationInFile
import io.strykermutator.jvm.language.Mutant
import io.strykermutator.jvm.language.MutantIdGenerator
import io.strykermutator.jvm.language.java.operator.JavaMutationOperator
import kotlin.jvm.optionals.getOrNull

internal class AstVisitor(
    private val operators: Collection<JavaMutationOperator>,
    private val mutantIdGenerator: MutantIdGenerator,
    private val collector: (Node, Mutant, Node) -> Unit
) : TreeVisitor() {

    override fun process(node: Node) {
        operators.forEach { operator ->
            operator.mutate(node).forEach { replacement ->
                val mutant = object : Mutant {
                    override val id: String = mutantIdGenerator.new()
                    override val location: ClosedRange<LocationInFile> = node.location
                    override val name: String = operator.name
                    override val replacement: String = replacement.toString()
                }
                collector(node, mutant, replacement)
            }
        }
    }

    private val Node.location: ClosedRange<LocationInFile>
        get() = checkNotNull(begin.getOrNull()).toLocationInFile()..checkNotNull(end.getOrNull()).toLocationInFile()

    private fun Position.toLocationInFile(): LocationInFile = LocationInFile(line, column)
}