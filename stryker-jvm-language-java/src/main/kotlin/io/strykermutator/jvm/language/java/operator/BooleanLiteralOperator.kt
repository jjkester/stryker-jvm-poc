package io.strykermutator.jvm.language.java.operator

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.BooleanLiteralExpr

public object BooleanLiteralOperator : JavaMutationOperator {

    override val name: String = "BooleanLiteral"

    override fun mutate(node: Node): Set<Node> {
        if (node is BooleanLiteralExpr) {
            return setOf(BooleanLiteralExpr(!node.value))
        }
        return emptySet()
    }
}