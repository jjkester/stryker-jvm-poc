package io.strykermutator.jvm.language.java.operator

import com.github.javaparser.ast.Node
import io.strykermutator.jvm.language.operator.SynchronousMutationOperator

public interface JavaMutationOperator : SynchronousMutationOperator<Node, Node>
