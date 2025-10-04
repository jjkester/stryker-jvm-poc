package io.strykermutator.jvm.language.java

import io.strykermutator.jvm.common.DefaultMutatableFile
import io.strykermutator.jvm.language.SequentialMutantIdGenerator
import io.strykermutator.jvm.common.StringCompanionMethodRef
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class JavaMutantGeneratorTest {

    @Test
    fun helloWorldTest() {
        val message = "Hello, World!"
        assertEquals("Hello, World!", message)
    }

    @Test
    fun testJavaMutantGenerator() {
        //Arrange
        val fileContent = """
            public class HelloWorld {
                public static void main(String[] args) {
                    if(true) {
                        System.out.println("Hello, World!");
                    } else {
                        System.out.println("Goodbye, World!");
                    }
                }
            }
        """.trimIndent()

        val sourceFile = File.createTempFile("HelloWorld", ".java")
        val targetFile = File.createTempFile("HelloWorld", ".java")
        sourceFile.writeText(fileContent)

        val mutantGenerator = JavaLanguagePlugin(
            StringCompanionMethodRef(
                "io.strykermutator.jvm.companion.StrykerCompanion",
                "mutantActive"
            )
        )

        //Act
        val mutants = mutantGenerator.instrument(DefaultMutatableFile(sourceFile, targetFile), SequentialMutantIdGenerator())

        println(mutants)

        //Assert
        assertEquals(1, mutants.size)
        val mutant = mutants.first()
        assertEquals("BooleanLiteral", mutant.operator)
        assertEquals("false", mutant.replacement)
        assertEquals("0", mutant.ref.id)
        assertEquals(3, mutant.location.segment.start.line)
        assertEquals(12, mutant.location.segment.start.column)
        assertEquals(3, mutant.location.segment.endInclusive.line)
        assertEquals(15, mutant.location.segment.endInclusive.column)

        assertEquals(
            """
            public class HelloWorld {

                public static void main(String[] args) {
                    if (io.strykermutator.jvm.companion.StrykerCompanion.mutantActive("0") ? false : true) {
                        System.out.println("Hello, World!");
                    } else {
                        System.out.println("Goodbye, World!");
                    }
                }
            }
            """.trimIndent(), targetFile.readText().trim()
        )

        sourceFile.deleteOnExit()
        targetFile.deleteOnExit()
    }

}