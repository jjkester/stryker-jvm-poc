import assertk.Assert
import assertk.assertions.support.expected
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.isRegularFile
import kotlin.io.path.walk

fun Assert<BuildResult>.task(taskPath: String): Assert<BuildTask?> = transform("$taskPath task") { it.task(taskPath) }

fun Assert<BuildResult>.tasks(): Assert<List<String>> =
    transform("task paths") { result -> result.tasks.map { it.path } }

fun Assert<BuildTask>.outcome(): Assert<TaskOutcome> = transform("outcome") { it.outcome }

@OptIn(ExperimentalPathApi::class)
fun Assert<Path>.recursiveChildren(): Assert<Sequence<Path>> = transform("recursive children") { path -> path.walk() }

fun Assert<Sequence<Path>>.files(): Assert<Sequence<Path>> =
    transform("files") { sequence -> sequence.filter { it.isRegularFile() } }

fun Assert<TaskOutcome>.isSuccessful(): Unit = given {
    if (it == TaskOutcome.FAILED) {
        expected("to be successful", actual = it)
    }
}
