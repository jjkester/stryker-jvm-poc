package io.strykermutator.jvm.gradle

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

public interface MutateSpec {
    @get:Input
    @get:Optional
    public val companionClass: Property<String>

    @get:Input
    @get:Optional
    public val companionMethod: Property<String>

    public fun copyTo(target: MutateSpec) {
        companionClass.set(target.companionClass)
        companionMethod.set(target.companionMethod)
    }
}
