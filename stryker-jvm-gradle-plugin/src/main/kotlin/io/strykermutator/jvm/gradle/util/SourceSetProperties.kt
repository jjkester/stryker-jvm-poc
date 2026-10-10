package io.strykermutator.jvm.gradle.util

import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.tasks.SourceSet
import kotlin.reflect.KCallable

internal fun SourceSet.configureFrom(other: SourceSet) {
    java.setSrcDirs(other.java.srcDirs)
    resources.setSrcDirs(other.resources.srcDirs)
    compileClasspath = other.compileClasspath
    runtimeClasspath = output + other.runtimeClasspath
}

internal fun extendSourceSetConfigurations(
    configurations: ConfigurationContainer,
    sourceSet: SourceSet,
    extendsFrom: SourceSet
) {
    sourceSetConfigurationNameGetters.forEach { configurationNameGetter ->
        configurations.findByName(configurationNameGetter.call(extendsFrom))?.also { configuration ->
            configurations.findByName(configurationNameGetter.call(sourceSet))?.extendsFrom(configuration)
        }
    }
}

private val sourceSetConfigurationNameGetters: Set<KCallable<String>> by lazy {
    @Suppress("UNCHECKED_CAST")
    SourceSet::class.members.asSequence()
        .filter { it.parameters.singleOrNull()?.type == SourceSet::class }
        .filter { it.returnType == String::class }
        .filter { it.name.startsWith("get") && it.name.endsWith("ConfigurationName") }
        .toSet() as Set<KCallable<String>>
}
