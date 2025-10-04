package io.strykermutator.jvm.language.java

import io.strykermutator.jvm.language.CompanionMethodRef
import io.strykermutator.jvm.language.Mutant
import io.strykermutator.jvm.language.MutantIdGenerator
import io.strykermutator.jvm.language.MutantInstrumenter
import io.strykermutator.jvm.language.java.operator.BooleanLiteralOperator
import io.strykermutator.jvm.language.java.operator.JavaMutationOperator
import io.strykermutator.jvm.language.java.placer.ExpressionMutantPlacer
import java.io.File

public class JavaMutantInstrumenter internal constructor(private val javaTransformer: JavaTransformer) :
    MutantInstrumenter {

    public constructor(companionMethodRef: CompanionMethodRef) : this(
        JavaTransformer(
            operators,
            ExpressionMutantPlacer(companionMethodRef)
        )
    )

    override fun supports(file: File): Boolean {
        return file.extension == "java"
    }

    override fun instrument(sourceFile: File, targetFile: File, mutantIdGenerator: MutantIdGenerator): Set<Mutant> =
        javaTransformer.transform(sourceFile, targetFile, mutantIdGenerator)

    private companion object {
        private val operators: Set<JavaMutationOperator> = setOf(BooleanLiteralOperator)
    }
}