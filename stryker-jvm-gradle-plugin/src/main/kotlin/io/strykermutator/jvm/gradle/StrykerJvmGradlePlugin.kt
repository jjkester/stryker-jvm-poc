package io.strykermutator.jvm.gradle

import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.Directory
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.create
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

            // Register mutation test task for main source set
            val mainSourceSet = extensions.getByType<SourceSetContainer>().named(SourceSet.MAIN_SOURCE_SET_NAME)
            tasks.register<MutationTestTask>(MutationTestTask.name(mainSourceSet.get())) {
                sourceSet.set(mainSourceSet)
            }

            // Register tasks
            project.registerTasks(extension, pluginConfiguration)
        }
    }

    private fun Project.registerTasks(extension: StrykerJvmExtension, pluginConfiguration: Provider<Configuration>) {
        registerLifecycleTask()

        // Register subtasks for each user-configured mutation test task
        project.afterEvaluate {
            tasks.withType<MutationTestTask>().forEach { mutationTestTask ->
                // Set default plugin configuration
                mutationTestTask.pluginConfiguration.convention(pluginConfiguration)

                registerSubTasks(mutationTestTask)
            }
        }
    }

    private fun Project.registerSubTasks(mutationTestTask: MutationTestTask) {
        // Finalize properties of mutation test task
        val sourceSet = mutationTestTask.sourceSet.apply { finalizeValue() }.get()
        val pluginConfiguration = mutationTestTask.pluginConfiguration.apply { finalizeValue() }.get()

        // Register mutate subtasks
        val mutateTask = registerMutateTask(sourceSet, pluginConfiguration, mutationTestTask)

        // Set task dependencies (mutation test task depends on subtasks)
        mutationTestTask.dependsOn(mutateTask)
    }

    private fun Project.registerMutateTask(
        sourceSet: SourceSet,
        pluginConfiguration: Configuration,
        mutateSpec: MutateSpec
    ): MutateTask {
        val targetDir = layout.strykerBuildDirectory("mutated-sources", sourceSet.name)

        return tasks.create<MutateTask>(MutateTask.name(sourceSet)) {
            sources.set(sourceSet.allSource)
            target.set(targetDir)
            classpath = pluginConfiguration
            mutateSpec.copyTo(this)
        }
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

    private fun Project.createStrykerJvmPluginConfiguration(): NamedDomainObjectProvider<Configuration> =
        configurations.register(PLUGIN_CONFIGURATION_NAME) { configuration ->
            configuration.apply {
                isVisible = false
                isCanBeConsumed = false
            }
        }

    private companion object {

        private const val EXTENSION_NAME = "strykerJvm"
        private const val PLUGIN_CONFIGURATION_NAME = "strykerJvmPlugin"
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
