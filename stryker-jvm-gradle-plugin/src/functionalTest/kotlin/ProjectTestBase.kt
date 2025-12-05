import assertk.Assert
import assertk.assertThat
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.AfterEach
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.deleteRecursively

abstract class ProjectTestBase(private val testCaseName: String) {

    /** Path to the directory containing the test project for this test class. */
    val testCaseDir: Path = testProjectDir.resolve(testCaseName)

    val projectDir: Path = testCaseDir

    val buildDir: Path = projectDir.resolve("build")

    fun gradleRunner(): GradleRunner = GradleRunner.create()
        .withProjectDir(projectDir.toFile())
        .withPluginClasspath()

    @AfterEach
    fun removeBuildDir() {
        @OptIn(ExperimentalPathApi::class)
        buildDir.deleteRecursively()
    }

    companion object {

        private val testProjectDir = Path(System.getProperty("testProjectDir"))
    }
}

fun ProjectTestBase.assertThatGradleRun(block: GradleRunner.() -> Unit): Assert<BuildResult> {
    return assertThat(gradleRunner().apply(block).build())
}
