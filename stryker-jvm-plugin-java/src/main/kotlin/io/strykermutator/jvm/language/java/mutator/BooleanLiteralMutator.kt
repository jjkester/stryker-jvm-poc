package io.strykermutator.jvm.language.java.mutator

import com.github.javaparser.ast.expr.BooleanLiteralExpr
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import io.strykermutator.jvm.language.LocationInFile
import io.strykermutator.jvm.language.Mutant

public class BooleanLiteralMutator : VoidVisitorAdapter<Set<Mutant>>() {

    private val idGenerator = generateSequence(0) { it + 1 }.iterator()

    override fun visit(n: BooleanLiteralExpr, arg: Set<Mutant>) {
        super.visit(n, arg)
        if(n.range.isEmpty) {
            return
        }

        val mutant = object : Mutant {
            override val id: String = idGenerator.next().toString()
            override val location: ClosedRange<LocationInFile> = n.range
                .map { range ->
                    LocationInFile(range.begin.line, range.begin.column)..
                            LocationInFile(range.end.line, range.end.column)
                }
                .get()
            override val name: String = "BooleanLiteralMutator"
            override val replacement: String = if (n.value) "false" else "true"
            override fun toString(): String {
                return "Mutant(id='$id', location=$location, name='$name', replacement='$replacement')"
            }
        }
        (arg as MutableSet).add(mutant)
    }

}