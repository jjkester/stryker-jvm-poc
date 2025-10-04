package io.strykermutator.jvm.language.java.placer

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.expr.Expression
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.expr.NameExpr
import com.github.javaparser.ast.expr.StringLiteralExpr
import io.strykermutator.jvm.language.CompanionMethodRef

public abstract class JavaMutantPlacer<T : Node>(private val companionMethodRef: CompanionMethodRef) :
    MutantPlacer<T, T> {

    protected fun companionMethodCall(id: String): Expression {
        return MethodCallExpr(
            NameExpr(companionMethodRef.qualifiedClassName),
            companionMethodRef.methodName,
            NodeList.nodeList(StringLiteralExpr(id))
        )
    }

    protected fun replaceInParent(old: Node, new: Node) {
        val r = old.replace(new)
        println("Replacement $r")
    }
}