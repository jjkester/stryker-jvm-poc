package io.strykermutator.jvm.language.java

import io.strykermutator.jvm.language.MutantGenerator
import org.junit.jupiter.api.Assertions.*
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

        val tempFile = File.createTempFile("HelloWorld", ".java")
        tempFile.writeText(fileContent)

        val mutantGenerator = JavaMutantGenerator()

        //Act
        val mutants = mutantGenerator.generate(tempFile)

        println(mutants)

        //Assert
        assertEquals(1, mutants.size);
        val mutant = mutants.first()
        assertEquals("BooleanLiteralMutator", mutant.name)
        assertEquals("false", mutant.replacement)
        assertEquals("0", mutant.id)
        assertEquals(3, mutant.location.start.line)
        assertEquals(12, mutant.location.start.column)
        assertEquals(3, mutant.location.endInclusive.line)
        assertEquals(15, mutant.location.endInclusive.column)

        tempFile.deleteOnExit()
    }

}