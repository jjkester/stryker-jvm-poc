package io.strykermutator.jvm.runner

import java.io.File

/**
 * Representation of a directory containing source code files that is the root of a compilation unit.
 */
public interface SourceRoot {

    /**
     * File pointing to the directory on a locally accessible filesystem.
     */
    public val file: File
}

/**
 * Default implementation of a representation of a directory containing source code files that is the root of a
 * compilation unit.
 *
 * Validates that the file points to an existing directory.
 *
 * @property file the file pointing to the directory on a locally accessible filesystem.
 * @constructor Creates a source root at the directory the file points to.
 */
public data class DefaultSourceRoot(override val file: File) : SourceRoot {

    init {
        require(file.exists()) { "Source root $file does not exist" }
        require(file.isDirectory) { "Source root $file is not a directory" }
    }
}
