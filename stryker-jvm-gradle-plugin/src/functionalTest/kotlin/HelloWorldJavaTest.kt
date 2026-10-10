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
                ":mutateMain",
                ":initialRunMainMutationTest",
            )
    }

    @Test
    fun testMutate() {
        assertThatGradleRun { withArguments("mutateMain") }
            .task(":mutateMain").isNotNull().outcome().isSuccessful()

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
        assertThatGradleRun { withArguments("initialRunMainMutationTest") }
            .task(":initialRunMainMutationTest").isNotNull().outcome().isSuccessful()

        val testReportDir = buildDir.resolve("test-results/initialRunMainMutationTest/")

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
