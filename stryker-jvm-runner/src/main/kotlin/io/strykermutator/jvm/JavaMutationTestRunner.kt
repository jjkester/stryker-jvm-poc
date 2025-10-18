package io.strykermutator.jvm

import io.strykermutator.jvm.common.DefaultMutantRef
import io.strykermutator.jvm.common.StringCompanionMethodRef
import io.strykermutator.jvm.core.*
import io.strykermutator.jvm.runner.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.apache.maven.shared.invoker.*
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory


public class JavaMutationTestRunner {

    private fun getMavenExecutable(): File {
        val mavenHome = System.getenv("MAVEN_HOME")
            ?: throw IllegalStateException("MAVEN_HOME environment variable is not set.")
        val mavenCmd = if (System.getProperty("os.name").lowercase().contains("win")) "mvn.cmd" else "mvn"
        return File(mavenHome, "bin/$mavenCmd")
    }

    public fun run(sourceRoot: SourceRoot) {
        val companionMethodRef =
            StringCompanionMethodRef("io.strykermutator.jvm.companion.StrykerCompanion", "mutantActive")
        val testRunner = StrykerFactory {
            instrumenter = DefaultInstrumenter(LanguagePlugins.load())
            executionPlanner = NaiveExecutionPlanner()
            reporter = DefaultReporter()
        }

        val targetDir = File("stryker-jvm-runner/build/stryker-mutated-sources")
        if (targetDir.exists()) {
            targetDir.deleteRecursively()
        }
        targetDir.mkdirs()

        val target = DefaultSourceRoot(targetDir)

        //Copy sources
        sourceRoot.file.copyRecursively(targetDir, overwrite = true)
        // Add companion dependency to copied pom.xml
        addCompanionDependencyToPom(File(targetDir, "pom.xml"))

        val instrumentationResult = testRunner.instrumentSources(
            DefaultSourceRoot(File(sourceRoot.file, "src/main")),
            DefaultSourceRoot(File(target.file, "src/main")),
            companionMethodRef
        )

        val mutantCoverageReport = dryRun(DefaultSourceRoot(targetDir))

        val testPlan = testRunner.planExecution(mutantCoverageReport)

        println("Mutants planned for execution: ${testPlan.testExecutions.flatMap { it.activeMutants }}")

        val report = testRunner.compileReport(
            mutants = instrumentationResult.mutants,
            mutantCoverageReport = mutantCoverageReport,
            testExecutionResults = emptyList()
        )

        println("Report: $report")
    }

    public fun dryRun(sourcePath: SourceRoot): MutantCoverageReport {
        val mavenExecutable = getMavenExecutable()
        val process = ProcessBuilder(
            mavenExecutable.absolutePath, "test"
        )
            .directory(sourcePath.file)
            .inheritIO()
            .start()
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("Dry-run Maven test run failed with exit code $exitCode")
        }

        val reportDir = File(sourcePath.file, "target/surefire-reports")
        val testCases = parseExecutedTestsFromXml(reportDir).also { executedTests ->
            println("Executed tests: $executedTests")
        }

        //now, run each test individually to see which classes/methods they cover
        val coverage = testCases.associateWith { openServerAndRunTest(it, sourcePath).map { DefaultMutantRef(it)}.toSet() }

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

    private fun openServerAndRunTest(testCase: TestCaseRef, sourceRoot: SourceRoot): Set<String> {
        val serverSocket = java.net.ServerSocket(0)
        serverSocket.soTimeout = 5000 // 5 seconds timeout for accept()
        val port = serverSocket.localPort
        println("Opened server socket on port $port for test ${testCase.name}")
        return runBlocking {
            val deferred = async(Dispatchers.IO) {
                try {
                    val client = serverSocket.accept() // will throw after 5s if no connection
                    val input = client.getInputStream().bufferedReader()
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
                    emptySet()
                } catch (e: Exception) {
                    try { serverSocket.close() } catch (_: Exception) {}
                    emptySet()
                }
            }

            val pomFile = File(sourceRoot.file, "pom.xml")
            val request: InvocationRequest = DefaultInvocationRequest()
            request.pomFile = pomFile
            request.baseDirectory = pomFile.parentFile
            // Only run the specific test case
            val testArg = "-Dtest=${testCase.name}"
            request.addArgs(mutableListOf<String?>(
                "test",
                testArg,
                "-Dstryker.coverage.port=$port"
            ))
            request.setOutputHandler { line -> println(line) }
            request.setErrorHandler { line -> System.err.println(line) }
            val invoker: Invoker = DefaultInvoker()
            invoker.mavenExecutable = getMavenExecutable()

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
    val sourceRoot = DefaultSourceRoot(File("stryker-jvm-runner/testProjects/helloWorld"))
    val runner = JavaMutationTestRunner()
    runner.run(sourceRoot)
}