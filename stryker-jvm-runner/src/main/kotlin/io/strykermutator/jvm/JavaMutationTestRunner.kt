package io.strykermutator.jvm

import io.strykermutator.jvm.common.DefaultMutantRef
import io.strykermutator.jvm.common.StringCompanionMethodRef
import io.strykermutator.jvm.core.*
import io.strykermutator.jvm.runner.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.apache.maven.shared.invoker.DefaultInvocationRequest
import org.apache.maven.shared.invoker.DefaultInvoker
import org.apache.maven.shared.invoker.InvocationRequest
import org.apache.maven.shared.invoker.InvocationResult
import org.apache.maven.shared.invoker.Invoker
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import javax.xml.parsers.DocumentBuilderFactory


public class JavaMutationTestRunner {

    public fun run(sourceRoot: SourceRoot) {
        val companionMethodRef =
            StringCompanionMethodRef("io.strykermutator.jvm.companion.StrykerCompanion", "mutantActive")
        val testRunner = StrykerFactory {
            instrumenter = DefaultInstrumenter(LanguagePlugins.load())
            executionPlanner = NaiveExecutionPlanner()
            reporter = DefaultReporter()
        }

        val targetDir = File("stryker-jvm-runner/build/stryker-mutated-sources")
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        } else {
            targetDir.deleteRecursively()
            targetDir.mkdirs()
        }
        val target = DefaultSourceRoot(targetDir)


        //Copy the full source root to a target directory, so we can instrument it there
        // Copy the full source root to the target directory by recursively copying all files and directories
//        sourceRoot.file.walkTopDown().forEach { file ->
//            val targetFile = File(target.file, file.relativeTo(sourceRoot.file).path)
//            if (file.isDirectory) {
//                targetFile.mkdirs()
//            } else {
//                file.copyTo(targetFile, overwrite = true)
//            }
//        }

        //Copy pom
        sourceRoot.file.copyRecursively(targetDir, overwrite = true)
        // Add companion dependency to copied pom.xml
        addCompanionDependencyToPom(File(targetDir, "pom.xml"))

        val instrumentationResult = testRunner.instrumentSources(
            DefaultSourceRoot(File(sourceRoot.file, "src/main")),
            DefaultSourceRoot(File(target.file, "src/main")),
            companionMethodRef
        )

        val mutantCoverageReport = DefaultMutantCoverageReport(mapOf())

        val testPlan = testRunner.planExecution(mutantCoverageReport)

        println("Mutants planned for execution: ${testPlan.testExecutions.flatMap { it.activeMutants }}")

        val report = testRunner.compileReport(
            mutants = instrumentationResult.mutants,
            mutantCoverageReport = mutantCoverageReport,
            testExecutionResults = emptyList()
        )

        println("Report: $report")
    }

    public fun dryRun(testSourcePath: SourceRoot): MutantCoverageReport {
        val process = ProcessBuilder(
            "C:\\Users\\JelleH\\M2_HOME\\bin\\mvn.cmd", "test"
        )
            .directory(File("C:\\Users\\JelleH\\IdeaProjects\\stryker-jvm-poc\\stryker-jvm-runner\\testProjects\\helloWorld"))
            .inheritIO()
            .start()
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("Dry-run Maven test run failed with exit code $exitCode")
        }

        val reportDir = File("stryker-jvm-runner/testProjects/helloWorld/target/surefire-reports")
        val testCases = parseExecutedTestsFromXml(reportDir).also { executedTests ->
            println("Executed tests: $executedTests")
        }

        //now, run each test individually to see which classes/methods they cover
        val coverage = testCases.associateWith { openServerAndRunTest(it).map { DefaultMutantRef(it)}.toSet() }

        return DefaultMutantCoverageReport(coverage)
    }

    private fun parseExecutedTestsFromXml(reportDir: File): Set<TestCaseRef> =
        reportDir.listFiles { file -> file.extension == "xml" }
            ?.flatMap { xmlFile ->
                val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile)
                val testCases = doc.getElementsByTagName("testcase")
                (0 until testCases.length).mapNotNull { i ->
                    val node = testCases.item(i)
                    val className = node.attributes.getNamedItem("classname")?.nodeValue
                    val methodName = node.attributes.getNamedItem("name")?.nodeValue
                    if (className != null && methodName != null) DefaultTestCaseRef(className, methodName) else null
                }
            }?.toSet() ?: emptySet()

    private fun openServerAndRunTest(testCase: TestCaseRef): Set<String> {
        val serverSocket = java.net.ServerSocket(0)
        serverSocket.soTimeout = 5000 // 5 seconds timeout for accept()
        val port = serverSocket.localPort
        return runBlocking {
            val deferred = async(Dispatchers.IO) {
                try {
                    println("collecting")
                    val client = serverSocket.accept() // will throw after 5s if no connection
                    println("collecting 2")
                    val input = client.getInputStream().bufferedReader()
                    println("collecting 3")
                    val result = mutableSetOf<String>()
                    input.forEachLine { line ->
                        if (line.isNotBlank()) result.add(line)
                    }
                    client.close()
                    serverSocket.close()
                    result
                } catch (e: java.net.SocketTimeoutException) {
                    println("Socket accept timed out, no coverage data received.")
                    try { serverSocket.close() } catch (_: Exception) {}
                    emptySet<String>()
                } catch (e: Exception) {
                    try { serverSocket.close() } catch (_: Exception) {}
                    emptySet<String>()
                }
            }

            val pomFile = File("C:\\Users\\JelleH\\IdeaProjects\\stryker-jvm-poc\\stryker-jvm-runner\\build\\stryker-mutated-sources\\pom.xml")
            val request: InvocationRequest = DefaultInvocationRequest()
            request.pomFile = pomFile
            request.baseDirectory = pomFile.parentFile
            // Only run the specific test case
            val testArg = "-Dtest=${testCase.name}"
            request.addArgs(mutableListOf<String?>(
                "test",
                testArg,
                "-Dstryker.coverage.port=$port",
                "-Dsurefire.additionalClasspath=C:\\Users\\JelleH\\IdeaProjects\\stryker-jvm-poc\\stryker-jvm-companion\\build\\classes"
            ))
            request.setOutputHandler { line -> println(line) }
            request.setErrorHandler { line -> System.err.println(line) }
            val invoker: Invoker = DefaultInvoker()
            invoker.mavenExecutable = File("C:\\Users\\JelleH\\M2_HOME\\bin\\mvn.cmd")

            println("MVN BUILD STARTING")
            val result: InvocationResult = invoker.execute(request)

            if (result.exitCode == 0) {
                println("Maven build succeeded")
            } else {
                println("Maven build failed")
            }
            // Await the deferred job, which will return emptySet if timed out or errored
            deferred.await()
        }
    }

    private fun addCompanionDependencyToPom(pomFile: File) {
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = docBuilder.parse(pomFile)
        val dependenciesList = doc.getElementsByTagName("dependencies")
        val dependenciesNode = if (dependenciesList.length > 0) {
            dependenciesList.item(0)
        } else {
            val projectNode = doc.getElementsByTagName("project").item(0)
            val newDependencies = doc.createElement("dependencies")
            projectNode.appendChild(newDependencies)
            newDependencies
        }
        val dependency = doc.createElement("dependency")
        val groupId = doc.createElement("groupId")
        groupId.textContent = "io.strykermutator"
        val artifactId = doc.createElement("artifactId")
        artifactId.textContent = "stryker-jvm-companion"
        val version = doc.createElement("version")
        version.textContent = "0.1.0-SNAPSHOT" // <-- Replace with actual version if needed
        dependency.appendChild(groupId)
        dependency.appendChild(artifactId)
        dependency.appendChild(version)
        dependenciesNode.appendChild(dependency)
        // Write changes back to file
        val transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes")
        transformer.transform(javax.xml.transform.dom.DOMSource(doc), javax.xml.transform.stream.StreamResult(pomFile))
    }

}

public fun main() {
    val testSourceRoot = DefaultSourceRoot(File("stryker-jvm-runner/testProjects/helloWorld/src/test/java"));
    val sourceRoot = DefaultSourceRoot(File("stryker-jvm-runner/testProjects/helloWorld"))
    val runner = JavaMutationTestRunner()
    runner.run(sourceRoot)
    val result = runner.dryRun(testSourceRoot)
    println("Dry run result: $result")
}