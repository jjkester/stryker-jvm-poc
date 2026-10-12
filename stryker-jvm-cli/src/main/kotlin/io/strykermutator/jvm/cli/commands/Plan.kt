package io.strykermutator.jvm.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import io.strykermutator.jvm.cli.mapper.toDomain
import io.strykermutator.jvm.cli.mapper.toDto
import io.strykermutator.jvm.common.dto.MutantCoverageReportDto
import io.strykermutator.jvm.core.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

internal class Plan : CliktCommand(name = "plan") {

    val mutationMutantCoverageReport by argument(name = "mutationMutantCoverageReport", help = "File to read the mutant coverage report from")
        .file(mustExist = true)

    override fun help(context: Context): String = "Show info about the environment"

    @OptIn(ExperimentalSerializationApi::class)
    override fun run() {
        val testRunner = StrykerFactory {
            instrumenter = DefaultInstrumenter(LanguagePlugins.load())
            executionPlanner = NaiveExecutionPlanner()
            reporter = DefaultReporter()
        }
        val report =
            Json.decodeFromStream<MutantCoverageReportDto>(mutationMutantCoverageReport.inputStream()).toDomain();
        val plan = testRunner.planExecution(report)
        echo(Json.encodeToString(plan.toDto()))
    }
}