package io.strykermutator.jvm.gradle

import io.strykermutator.jvm.gradle.tasks.InitialRunTask
import io.strykermutator.jvm.gradle.tasks.MutateTask
import io.strykermutator.jvm.gradle.tasks.MutationTestTask
import io.strykermutator.jvm.gradle.util.configureFrom
import io.strykermutator.jvm.gradle.util.extendSourceSetConfigurations
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.Directory
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.language.base.plugins.LifecycleBasePlugin

public class StrykerJvmGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            // Create extension for global configuration
            val extension = createStrykerJvmExtension()

            // Create configuration for plugin classpath
            val pluginConfiguration = createStrykerJvmPluginConfiguration()

            // Create configuration for companion classpath
            val companionConfiguration = createStrykerJvmCompanionConfiguration()

            // Register mutation test task for main source set
            val mainSourceSet = sourceSets.named(SourceSet.MAIN_SOURCE_SET_NAME)
            tasks.register<MutationTestTask>(MutationTestTask.name(SourceSet.MAIN_SOURCE_SET_NAME)) {
                sourceSet.set(mainSourceSet)
                testSourceSet.set(sourceSets.named(SourceSet.TEST_SOURCE_SET_NAME))
                testTask.set(tasks.named("test", Test::class.java))
            }

            // Register tasks
            project.registerTasks(extension, pluginConfiguration, companionConfiguration)
        }
    }

    private fun Project.registerTasks(
        extension: StrykerJvmExtension,
        pluginConfiguration: Provider<Configuration>,
        companionConfiguration: Provider<Configuration>
    ) {
        registerLifecycleTask()

        // Register subtasks for each user-configured mutation test task
        project.afterEvaluate {
            tasks.withType<MutationTestTask>().forEach { mutationTestTask ->
                // Set default plugin configuration
                mutationTestTask.pluginConfiguration.convention(pluginConfiguration)
                mutationTestTask.companionConfiguration.convention(companionConfiguration)

                registerSubTasks(mutationTestTask)
            }
        }
    }

    private fun Project.registerSubTasks(mutationTestTask: MutationTestTask) {
        // Determine target directory
        val targetDir = mutationTestTask.sourceSet.flatMap {
            layout.strykerBuildDirectory("mutated-sources", it.name)
        }

        // Register mutate subtasks
        val mutateTask = registerMutateTask(
            mutationTestName = mutationTestTask.name,
            sourceSet = mutationTestTask.sourceSet,
            pluginConfiguration = mutationTestTask.pluginConfiguration,
            mutateSpec = mutationTestTask,
            targetDir = targetDir
        )

        // Register mutated source set
        val mutatedSourceSet = registerMutatedSourceSet(
            mutationTestName = mutationTestTask.name,
            originalMainSourceSet = mutationTestTask.sourceSet,
            targetDir = targetDir,
            mutateTask = mutateTask,
            companionConfiguration = mutationTestTask.companionConfiguration
        )

        // Register execute task
        val initialRunTask = registerInitialRunTask(
            mutationTestName = mutationTestTask.name,
            originalSourceSet = mutationTestTask.sourceSet,
            mutatedSourceSet = mutatedSourceSet,
            testSourceSet = mutationTestTask.testSourceSet,
            testTask = mutationTestTask.testTask,
            companionConfiguration = mutationTestTask.companionConfiguration
        )

        // Set task dependencies
        initialRunTask.configure { it.dependsOn(mutateTask) }
        mutationTestTask.dependsOn(initialRunTask) // Initial run is current last step
    }

    private fun Project.registerMutateTask(
        mutationTestName: String,
        sourceSet: Provider<SourceSet>,
        pluginConfiguration: Provider<Configuration>,
        mutateSpec: MutateSpec,
        targetDir: Provider<Directory>
    ): TaskProvider<MutateTask> = tasks.register<MutateTask>(MutateTask.name(mutationTestName)) {
        sources.set(sourceSet.map { it.allSource })
        target.set(targetDir)
        classpath = project.files(pluginConfiguration)
        mutateSpec.copyTo(this)
    }

    private fun Project.registerMutatedSourceSet(
        mutationTestName: String,
        originalMainSourceSet: Provider<SourceSet>,
        targetDir: Provider<Directory>,
        mutateTask: TaskProvider<MutateTask>,
        companionConfiguration: Provider<Configuration>
    ): Provider<SourceSet> = sourceSets.register("${mutationTestName}MutationTest") { sourceSet ->
        sourceSet.apply {
            // Copy properties from original source set
            configureFrom(originalMainSourceSet.get())
            extendSourceSetConfigurations(configurations, this, originalMainSourceSet.get())

            // Add companion to compilation
            compileClasspath += project.files(companionConfiguration)

            // Point to mutated sources and set task dependency
            java.setSrcDirs(listOf(targetDir))
            tasks.findByName(compileJavaTaskName)?.dependsOn(mutateTask)
        }
    }

    private fun Project.registerInitialRunTask(
        mutationTestName: String,
        originalSourceSet: Provider<SourceSet>,
        mutatedSourceSet: Provider<SourceSet>,
        testSourceSet: Provider<SourceSet>,
        testTask: Property<Test>,
        companionConfiguration: Provider<Configuration>
    ): TaskProvider<InitialRunTask> = tasks.register<InitialRunTask>(InitialRunTask.name(mutationTestName)) {
        configureFrom(testTask.get())
        this.testSourceSet.set(testSourceSet)
        this.originalSourceSet.set(originalSourceSet)
        this.mutatedSourceSet.set(mutatedSourceSet)
        this.companionClasspath.set(companionConfiguration)
    }

    private fun Project.registerLifecycleTask(): TaskProvider<Task> {
        return tasks.register<Task>(LIFECYCLE_TASK_NAME) {
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            description = "Runs mutation testing for the test suite."

            // Register dependencies with all mutation test tasks
            dependsOn(tasks.withType<MutationTestTask>())
        }
    }

    private fun Project.createStrykerJvmExtension(): StrykerJvmExtension =
        extensions.create(EXTENSION_NAME, StrykerJvmExtension::class.java).apply {
            // Apply conventions (sensible defaults)
        }

    private fun Project.createStrykerJvmPluginConfiguration(): Provider<Configuration> =
        configurations.register(PLUGIN_CONFIGURATION_NAME) { configuration ->
            configuration.apply {
                isVisible = false
                isCanBeConsumed = false
            }
        }

    private fun Project.createStrykerJvmCompanionConfiguration(): Provider<Configuration> =
        configurations.register(COMPANION_CONFIGURATION_NAME) { configuration ->
            configuration.apply {
                isVisible = false
                isCanBeConsumed = true
            }
        }

    private companion object {

        private const val EXTENSION_NAME = "strykerJvm"
        private const val PLUGIN_CONFIGURATION_NAME = "strykerJvmPlugin"
        private const val COMPANION_CONFIGURATION_NAME = "strykerJvmCompanion"
        private const val LIFECYCLE_TASK_NAME = "mutationTest"

        private fun ProjectLayout.strykerBuildDirectory(name: String, vararg names: String): Provider<Directory> = names
            .fold(
                initial = buildDirectory
                    .dir("stryker")
                    .map { it.dir(name) }
            ) { dirProvider, name ->
                dirProvider.map { it.dir(name) }
            }
    }
}

private val Project.sourceSets: SourceSetContainer
    get() = extensions.getByType<SourceSetContainer>()
