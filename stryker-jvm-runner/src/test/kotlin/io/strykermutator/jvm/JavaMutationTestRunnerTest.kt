package io.strykermutator.jvm

import org.junit.jupiter.api.Test
import java.nio.file.Paths

class JavaMutationTestRunnerTest {

    @Test
    fun test() {
        //Arrange
        val path = Paths.get("testProjects", "helloWorld", "src", "main", "java").toAbsolutePath().toString()

        //Act
        val runner = JavaMutationTestRunner().run(path);

        //Assert
        //TODO: assert result
    }

}