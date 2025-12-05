import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.exists
import assertk.assertions.isDirectory
import assertk.assertions.isNotNull
import org.junit.jupiter.api.Test

class HelloWorldJavaTest : ProjectTestBase("hello-world") {

    @Test
    fun testTasks() {
        assertThatGradleRun { withArguments("mutationTest") }
            .tasks()
            .containsExactlyInAnyOrder(":mutationTest", ":mutationTestMain", ":mutateMain")
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

}
