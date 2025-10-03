package io.strykermutator.jvm.language.java

import com.github.javaparser.StaticJavaParser
import io.strykermutator.jvm.language.Mutant
import io.strykermutator.jvm.language.MutantGenerator
import io.strykermutator.jvm.language.java.mutator.BooleanLiteralMutator
import java.io.File

public class JavaMutantGenerator : MutantGenerator {

    override fun supports(file: File): Boolean {
        return true
    }

    override fun generate(file: File): Set<Mutant> {
        val cu = StaticJavaParser.parse(file);
        val mutantVisitor = BooleanLiteralMutator();
        val result = mutableSetOf<Mutant>();
        mutantVisitor.visit(cu, result);

        return result;
    }

    public companion object {

        @JvmStatic
        public fun generateGuardedReplacementExpression(id: String, original: String, replacement: String): String {
            return "io.strykermutator.jvm.companion.StrykerCompanion.mutantActive(\"$id\")) ? $replacement : $original"
        }

    }
}