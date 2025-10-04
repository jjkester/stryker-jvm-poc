package io.strykermutator.jvm.language.java.placer

import com.github.javaparser.ast.expr.ConditionalExpr
import com.github.javaparser.ast.expr.Expression
import io.strykermutator.jvm.common.CompanionMethodRef
import io.strykermutator.jvm.common.Mutant

public object ExpressionMutantPlacer : JavaMutantPlacer<Expression>() {

    override fun place(
        node: Expression,
        mutants: Map<Mutant, Expression>,
        companionMethodRef: CompanionMethodRef
    ) {
        val replacement = mutants.entries.fold(node.clone()) { accumulator, (mutant, replacement) ->
            ConditionalExpr(companionMethodCall(companionMethodRef, mutant.ref), replacement, accumulator)
        }
        node.replace(replacement)
    }
}