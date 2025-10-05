package io.strykermutator.jvm.language.java.placer

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.expr.Expression
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.expr.NameExpr
import com.github.javaparser.ast.expr.StringLiteralExpr
import io.strykermutator.jvm.common.CompanionMethodRef
import io.strykermutator.jvm.common.MutantRef
import io.strykermutator.jvm.language.placer.MutantPlacer

public abstract class JavaMutantPlacer<T : Node> : MutantPlacer<T, T> {

    protected fun companionMethodCall(companionMethodRef: CompanionMethodRef, mutantRef: MutantRef): Expression {
        return MethodCallExpr(
            NameExpr(companionMethodRef.qualifiedClassName),
            companionMethodRef.methodName,
            NodeList.nodeList(StringLiteralExpr(mutantRef.id))
        )
    }
}