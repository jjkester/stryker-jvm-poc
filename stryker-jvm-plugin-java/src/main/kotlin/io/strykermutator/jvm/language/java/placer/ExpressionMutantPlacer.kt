package io.strykermutator.jvm.language.java.placer

import com.github.javaparser.ast.expr.ConditionalExpr
import com.github.javaparser.ast.expr.Expression
import io.strykermutator.jvm.language.CompanionMethodRef
import io.strykermutator.jvm.language.Mutant

public class ExpressionMutantPlacer(companionMethodRef: CompanionMethodRef) :
    JavaMutantPlacer<Expression>(companionMethodRef) {

    override fun place(
        node: Expression,
        mutants: Map<Mutant, Expression>
    ) {
        val replacement = mutants.entries.fold(node.clone()) { accumulator, (mutant, replacement) ->
            ConditionalExpr(companionMethodCall(mutant.id), replacement, accumulator)
        }
        replaceInParent(node, replacement)
    }
}