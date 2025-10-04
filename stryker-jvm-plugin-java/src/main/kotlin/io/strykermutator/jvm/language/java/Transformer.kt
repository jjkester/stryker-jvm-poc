package io.strykermutator.jvm.language.java

import io.strykermutator.jvm.language.Mutant
import io.strykermutator.jvm.language.MutantIdGenerator
import java.io.File

public interface Transformer {

    public fun transform(sourceFile: File, targetFile: File, mutantIdGenerator: MutantIdGenerator): Set<Mutant>
}
