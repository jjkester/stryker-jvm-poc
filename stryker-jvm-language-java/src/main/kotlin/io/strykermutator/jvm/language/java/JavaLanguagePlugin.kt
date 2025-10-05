package io.strykermutator.jvm.language.java

import io.strykermutator.jvm.common.Mutant
import io.strykermutator.jvm.common.MutatableFile
import io.strykermutator.jvm.language.LanguagePlugin
import io.strykermutator.jvm.language.MutationConfiguration
import io.strykermutator.jvm.language.java.operator.BooleanLiteralOperator
import io.strykermutator.jvm.language.java.operator.JavaMutationOperator
import io.strykermutator.jvm.language.java.placer.ExpressionMutantPlacer

public class JavaLanguagePlugin internal constructor(private val javaTransformer: JavaTransformer) :
    LanguagePlugin {

    override val name: String = "java"

    public constructor() : this(
        JavaTransformer(operators, ExpressionMutantPlacer)
    )

    override fun supports(file: MutatableFile): Boolean {
        return file.extension == "java"
    }

    override fun instrument(file: MutatableFile, configuration: MutationConfiguration): Set<Mutant> =
        javaTransformer.transform(file, configuration)

    private companion object {
        private val operators: Set<JavaMutationOperator> = setOf(BooleanLiteralOperator)
    }
}