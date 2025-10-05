package io.strykermutator.jvm.core

import io.strykermutator.jvm.common.MutatableFile
import io.strykermutator.jvm.core.internal.PluginLoader
import io.strykermutator.jvm.language.LanguagePlugin

/**
 * Collection of [language plugins][LanguagePlugin].
 */
public interface LanguagePlugins : Collection<LanguagePlugin> {

    /**
     * Names of the available language plugins.
     */
    public val names: Set<String>

    /**
     * Finds and returns the language plugin with the provided [name] if one is found.
     *
     * @param name name of the language plugin to find.
     * @returns the language plugin for the provided [name] if one was found.
     */
    public fun byName(name: String): LanguagePlugin?

    /**
     * Finds and returns the language plugin that supports the provided [file] if one is found.
     *
     * @param file file to find a supported language plugin for.
     * @returns the language plugin for the provided [file] if one was found.
     * @throws IllegalStateException when more than one plugin supports the provided [file].
     */
    public fun supportingFile(file: MutatableFile): LanguagePlugin?

    public companion object {

        @JvmStatic
        public fun of(instances: Iterable<LanguagePlugin>): LanguagePlugins =
            DefaultLanguagePlugins(instances.toSet())

        @JvmStatic
        public fun of(vararg instances: LanguagePlugin): LanguagePlugins =
            DefaultLanguagePlugins(instances.toSet())

        /**
         * Loads the available language plugins from the classpath.
         */
        @JvmStatic
        public fun load(): LanguagePlugins = of(PluginLoader.load())
    }
}

/**
 * Alias for [LanguagePlugins.byName].
 *
 * @see LanguagePlugins.byName
 */
public operator fun LanguagePlugins.get(name: String): LanguagePlugin? = byName(name)

/**
 * Alias for [LanguagePlugins.supportingFile].
 *
 * @see LanguagePlugins.supportingFile
 */
public operator fun LanguagePlugins.get(file: MutatableFile): LanguagePlugin? = supportingFile(file)

@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
internal class DefaultLanguagePlugins(
    private val instances: Set<LanguagePlugin>
) : LanguagePlugins, Collection<LanguagePlugin> by instances {

    private val instancesByName: Map<String, LanguagePlugin> = instances.associateBy { it.name }

    override val names: Set<String>
        get() = instancesByName.keys

    init {
        require(names.size == instances.size) { "Language plugins must have unique names" }
    }

    override fun byName(name: String): LanguagePlugin? = instancesByName[name]

    override fun supportingFile(file: MutatableFile): LanguagePlugin? {
        val supported = instances.filter { it.supports(file) }
        check(supported.size < 2) { "Found multiple language plugins supporting file $file" }
        return supported.singleOrNull()
    }
}