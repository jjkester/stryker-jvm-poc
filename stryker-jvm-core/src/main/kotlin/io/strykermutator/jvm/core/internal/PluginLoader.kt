package io.strykermutator.jvm.core.internal

import java.util.*

internal class PluginLoader<T : Any>(private val serviceLoader: ServiceLoader<T>) : Iterable<T> by serviceLoader {

    companion object {

        inline fun <reified T : Any> load(): PluginLoader<T> = PluginLoader(ServiceLoader.load(T::class.java))
    }
}
