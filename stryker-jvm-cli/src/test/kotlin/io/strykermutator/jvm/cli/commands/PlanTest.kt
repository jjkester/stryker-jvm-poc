package io.strykermutator.jvm.cli.commands

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.ajalt.clikt.testing.test
import io.strykermutator.jvm.common.dto.MutationTestPlanDto
import io.strykermutator.jvm.common.dto.TestExecutionDto
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.io.path.writeText

class PlanTest {

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `reads coverage report`() {
        val report = tempDir.resolve("coverage.json")
        report.writeText(
            //language=json
            """
            {
              "coverage": {
                "ClassA#testCase1": {
                  "mutantIds": ["a", "b", "c"]
                },
                "ClassB#testCase2": {
                  "mutantIds": ["b", "c", "d"]
                }
              }
            }
        """
        )

        val result = Plan().test(report.toString())
        // Assert on result.output or result.status
        assertThat(result.statusCode).isEqualTo(0);
        val plan = Json.decodeFromString<MutationTestPlanDto>(result.output);
        assertThat(plan).isEqualTo(MutationTestPlanDto(setOf(
            TestExecutionDto(setOf("ClassA#testCase1", "ClassB#testCase2"), setOf("a")),
            TestExecutionDto(setOf("ClassA#testCase1", "ClassB#testCase2"), setOf("b")),
            TestExecutionDto(setOf("ClassA#testCase1", "ClassB#testCase2"), setOf("c")),
            TestExecutionDto(setOf("ClassA#testCase1", "ClassB#testCase2"), setOf("d")),
        )))
    }
}