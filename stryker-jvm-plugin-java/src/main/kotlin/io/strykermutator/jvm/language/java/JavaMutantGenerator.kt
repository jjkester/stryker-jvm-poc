package io.strykermutator.jvm.language.java

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.visitor.VoidVisitor
import io.strykermutator.jvm.language.Mutant
import io.strykermutator.jvm.language.MutantGenerator
import io.strykermutator.jvm.language.java.mutator.BooleanLiteralMutator
import java.io.File
import java.nio.file.Files

public class JavaMutantGenerator : MutantGenerator  {

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


}