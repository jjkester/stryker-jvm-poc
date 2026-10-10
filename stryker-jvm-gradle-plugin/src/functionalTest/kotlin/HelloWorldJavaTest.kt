import assertk.all
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.Test

class HelloWorldJavaTest : ProjectTestBase("hello-world") {

    @Test
    fun testTasks() {
        assertThatGradleRun { withArguments("mutationTest") }
            .tasks()
            .containsAtLeast(
                ":mutationTest",
                ":mutationTestMain",
                ":mutateMutationTestMain",
                ":initialRunMutationTestMain",
            )
    }

    @Test
    fun testMutate() {
        val taskName = "mutateMutationTestMain"
        assertThatGradleRun { withArguments(taskName) }
            .task(":$taskName").isNotNull().outcome().isSuccessful()

        val mutatedSourcesDir = buildDir.resolve("stryker/mutated-sources/main")
        val mutatedHelloWorldFile = mutatedSourcesDir.resolve("com/example/HelloWorld.java")
        val mutatedNewClassFile = mutatedSourcesDir.resolve("com/example/NewClass.java")

        assertThat(mutatedSourcesDir).all {
            exists()
            isDirectory()
            recursiveChildren()
                .files()
                .containsExactlyInAnyOrder(mutatedHelloWorldFile, mutatedNewClassFile)
        }
    }

    @Test
    fun testInitialRun() {
        val taskName = "initialRunMutationTestMain"

        assertThatGradleRun { withArguments(taskName) }
            .task(":$taskName").isNotNull().outcome().isSuccessful()

        val testReportDir = buildDir.resolve("test-results/$taskName/")

        assertThat(testReportDir).all {
            exists()
            isDirectory()
            children()
                .files()
                .fileNames()
                .containsExactlyInAnyOrder(
                    "TEST-com.example.HelloWorldTest.xml",
                    $$"TEST-com.example.HelloWorldTest$NestedHelloWorldTest.xml"
                )
        }
    }

}
